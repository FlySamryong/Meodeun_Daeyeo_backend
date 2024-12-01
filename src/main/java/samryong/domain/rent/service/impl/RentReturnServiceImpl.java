package samryong.domain.rent.service.impl;

import static samryong.domain.chat.entity.ChatMessage.ChatType.*;
import static samryong.domain.rent.entity.Rent.RentStatus.*;
import static samryong.global.code.GlobalErrorCode.*;

import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import samryong.domain.account.service.AccountService;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.item.converter.ItemConverter;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.Item.Status;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.item.repository.elastic.ItemElasticRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.service.RentReturnService;
import samryong.domain.rent.service.RentService;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RentReturnServiceImpl implements RentReturnService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RentService rentService;
    private final ChatMessageService chatMessageService;
    private final AccountService accountService;
    private final ItemRepository itemRepository;
    private final ItemElasticRepository itemElasticRepository;

    private static final String OTP = "OTP:";
    private static final String RENT = "RENT:";
    private static final int OTP_EXPIRATION_MINUTES = 5;

    private static final String ITEM_RETURN_MESSAGE = "물품 반납이 승인되었습니다. 물품 상태를 확인 후 보증금을 반환해주세요.";
    private static final String DEPOSIT_RETURN_MESSAGE = "보증금이 반환되었습니다.";
    private static final String DEPOSIT_REJECT_MESSAGE = "물품 손상으로 인한 보증금 반환이 거부되었습니다.";

    // 반납 진행을 위한 OTP 생성
    @Override
    @Transactional
    public String generateOTP(Member owner, Long roomId, Long rentId) {

        // 1. 대여 정보 검증
        rentService.getValidatedChatRoomForOwner(owner, roomId);

        // 2. 6자리 OTP 생성
        SecureRandom random = new SecureRandom();
        int otpNumber = 100000 + random.nextInt(900000);

        // 3. Redis에 OTP 저장
        redisTemplate
                .opsForValue()
                .set(OTP + rentId, String.valueOf(otpNumber), OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        // 4. OTP 반환
        return String.valueOf(otpNumber);
    }

    // OTP 검증 및 대여 정보 변경
    @Override
    @Transactional
    public void verifyOTP(Member renter, Long roomId, Long rentId, String otp) {

        // 1. 대여 정보 검증
        rentService.getValidatedChatRoomForRenter(renter, roomId);

        // 2. OTP 검증
        String redisOTP =
                (String)
                        Optional.ofNullable(redisTemplate.opsForValue().get(OTP + rentId))
                                .orElseThrow(() -> new GlobalException(OTP_NOT_MATCH));

        if (!otp.equals(redisOTP)) {
            throw new GlobalException(OTP_NOT_MATCH);
        }

        // 3. OTP 삭제
        if (Boolean.TRUE.equals(redisTemplate.hasKey(OTP + rentId))) {
            redisTemplate.delete(OTP + rentId);
        }

        // 4. 대여 정보 및 물품 정보 변경
        Rent rent = rentService.getRent(rentId);
        Item item = rent.getItem();
        item.setStatus(Status.AVAILABLE);
        rentService.changeRentStatus(rent, null, null, RETURN_ACCEPT);
        itemRepository.save(item);
        itemElasticRepository.save(ItemConverter.toItemDocument(item));

        // 5. Redis에서 대여 정보 삭제
        if (Boolean.TRUE.equals(redisTemplate.hasKey(RENT + rentId))) {
            redisTemplate.delete(RENT + rentId);
        }

        // 6. 물품 등록자에게 메시지 전송, 보증금을 반환해야 함을 알림
        chatMessageService.sendRentCommonMessage(renter, roomId, ITEM_RETURN_MESSAGE, DEPOSIT_REQ);
    }

    // 보증금 반환
    @Override
    @Transactional
    public void returnDeposit(Member owner, Long roomId, Long rentId, Boolean isReturn) {

        // 1. 대여 정보 검증
        rentService.getValidatedChatRoomForOwner(owner, roomId);
        Rent rent = rentService.getRent(rentId);

        // 2. 보증금 반환 여부
        Long deposit = rent.getItem().getDeposit();
        if (isReturn) { // 물품 손상이 없으면 보증금 반환
            accountService.receiveTransfer(rent.getRenter(), deposit);
            chatMessageService.sendRentCommonMessage(owner, roomId, DEPOSIT_RETURN_MESSAGE, DEPOSIT_RES);
        } else { // 물품 손상이 있으면 보증금 반환 거부
            accountService.receiveTransfer(owner, deposit);
            chatMessageService.sendRentCommonMessage(owner, roomId, DEPOSIT_REJECT_MESSAGE, DEPOSIT_RES);
        }
    }
}
