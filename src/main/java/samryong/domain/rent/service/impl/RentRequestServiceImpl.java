package samryong.domain.rent.service.impl;

import static samryong.domain.chat.entity.ChatMessage.ChatType.*;
import static samryong.domain.rent.entity.Rent.RentStatus.*;
import static samryong.global.code.GlobalErrorCode.*;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.service.AccountService;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.item.converter.ItemConverter;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.item.repository.elastic.ItemElasticRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.domain.member.service.MemberService;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;
import samryong.domain.rent.service.RentRequestService;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RentRequestServiceImpl implements RentRequestService {

    private final RentServiceImpl rentService;
    private final AccountService accountService;
    private final ChatMessageService chatMessageService;
    private final ItemRepository itemRepository;
    private final ItemElasticRepository itemElasticRepository;
    private final MemberService memberService;

    private static final int DEFAULT_REDIS_EXPIRATION_HOURS = 12;
    private static final int DEFAULT_PLUS_HOURS = 2;
    private static final int DEFAULT_MINUS_HOURS = 1;
    private static final String RENT_REQUEST_MESSAGE = "대여료를 송금했습니다. 12시간 내로 확인해주세요.";
    private static final String RENT_ACCEPT_MESSAGE = "물품 대여 요청이 수락되었습니다. 최종 확인 후 대여 진행해주세요.";
    private static final String RENT_PROCESS_MESSAGE = "대여 진행이 시작됩니다. 사용자의 계좌로 입금된 대여료를 확인해주세요.";
    private final MemberRepository memberRepository;

    // 대여료 송금하기
    @Override
    @Transactional
    public Long sendRentFee(Member renter, Long roomId, Long fee) {

        // 1. 대여 정보 검증
        ChatRoom chatRoom = rentService.getValidatedChatRoomForRenter(renter, roomId);

        // 2. 대여 가능 여부 확인
        Item item = chatRoom.getItem();
        rentService.checkItemAvailability(item);

        // 3. 대여료 송금, 대여료 + 보증금 송금
        Long totalAmount = fee + item.getDeposit();
        accountService.drawTransfer(renter, chatRoom.getOwner(), totalAmount);

        // 4. 대여 정보 생성
        Rent rent = rentService.createRent(chatRoom, item, fee);
        Long rentId = rent.getId();
        itemRepository.save(item);

        // 5. Redis에 대여 정보 저장, 12시간 후 만료
        rentService.saveRentKey(rent.getId(), roomId, DEFAULT_REDIS_EXPIRATION_HOURS, TimeUnit.HOURS);

        // 6. 대여 정보 메시지 전송
        chatMessageService.sendRentRequestMessage(
                renter, roomId, rentId, RENT_REQUEST_MESSAGE, RENT_REQ);

        return rentId;
    }

    // 대여 수락하기
    @Override
    @Transactional
    public Long acceptRent(Member owner, Long roomId, Long rentId, LocalDateTime endDate) {

        // 1. 대여 정보 검증
        ChatRoom chatRoom = rentService.getValidatedChatRoomForOwner(owner, roomId);
        if (endDate.isBefore(LocalDateTime.now())) {
            throw new GlobalException(INVALID_RENT_STATUS);
        }

        // 2. 대여 가능한 물품인지 확인
        Item item = chatRoom.getItem();
        rentService.checkItemAvailability(item);
        rentService.validateRentIdInRedis(roomId, rentId);

        // 3. 대여 정보 변경
        Rent rent = rentService.getRent(rentId);
        rentService.changeRentStatus(rent, LocalDateTime.now(), endDate, RentStatus.ACCEPT);

        // 4. 물품 상태 변경
        item.setStatus(Item.Status.RENTED); // 다른 사람이 대여할 수 없도록 변경

        // 5. Redis에 대여 정보 저장, 12시간 후 만료
        rentService.saveRentKey(
                rentId, roomId, DEFAULT_REDIS_EXPIRATION_HOURS, TimeUnit.HOURS); // 최종 승인까지 대여 정보 저장

        // 6. 물품 상태 변경 저장
        itemRepository.save(item);
        itemElasticRepository.save(ItemConverter.toItemDocument(item));

        // 7. 대여 정보 메시지 전송
        chatMessageService.sendRentCommonMessage(owner, roomId, RENT_ACCEPT_MESSAGE, RENT_ACCEPT);
        return rentId;
    }

    // 최종 대여 동의
    @Override
    @Transactional
    public Long acceptRentProcess(Member renter, Long roomId, Long rentId) {

        // 1. 대여 정보 검증
        ChatRoom chatRoom = rentService.getValidatedChatRoomForRenter(renter, roomId);
        rentService.validateRentIdInRedis(roomId, rentId);
        Rent rent = rentService.getRent(rentId);
        if (rent.getStatus() != RentStatus.ACCEPT) {
            throw new GlobalException(INVALID_RENT_STATUS);
        }

        // 2. 대여 정보 변경
        rentService.changeRentStatus(rent, null, null, RENT_PROCESS);

        // 3. 대여료 입금 처리, 보증금을 제외한 대여료만 입금, 보증금은 대여 종료 후 반환
        Member owner = chatRoom.getOwner();
        accountService.receiveTransfer(owner, rent.getRentFee());

        // 4. Redis에 대여 정보 저장, 반납일로부터 2시간 후에 만료, 알림을 위한 키 저장
        LocalDateTime expirationDate = rent.getEndDate().plusHours(DEFAULT_PLUS_HOURS);
        LocalDateTime expirationForNotice = rent.getEndDate().minusHours(DEFAULT_MINUS_HOURS);
        rentService.saveRentKey(rentId, roomId, expirationDate);
        if (expirationForNotice.isAfter(LocalDateTime.now()))
            rentService.saveNoticeKey(rentId, roomId, expirationForNotice);

        // 5. 사용자의 대여 정보 업데이트
        memberService.updateRentList(renter, rent);
        memberService.updateLoanList(owner, rent);
        memberRepository.save(renter);
        memberRepository.save(owner);

        // 6. 대여 정보 메시지 전송
        chatMessageService.sendRentCommonMessage(renter, roomId, RENT_PROCESS_MESSAGE, RENT_AGREE);

        return rentId;
    }
}
