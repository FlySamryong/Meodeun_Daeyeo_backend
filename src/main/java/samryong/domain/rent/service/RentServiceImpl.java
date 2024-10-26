package samryong.domain.rent.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import samryong.domain.account.service.AccountService;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.chat.service.ChatRoomService;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.converter.RentConverter;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;
import samryong.domain.rent.repository.RentRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final ChatRoomService chatRoomService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageService chatMessageService;
    private final AccountService accountService;
    private final ItemRepository itemRepository;

    private static final String RENT = "RENT:";
    private static final int DEFAULT_REDIS_EXPIRATION_HOURS = 12;

    // 대여료 송금하기
    @Override
    @Transactional
    public Long sendRentFee(Member renter, Long roomId, Long fee) {
        ChatRoom chatRoom = getValidatedChatRoomForRenter(renter, roomId);
        Item item = chatRoom.getItem();
        Long totalAmount = fee + item.getDeposit();

        accountService.drawTransfer(renter, chatRoom.getOwner(), totalAmount); // 대여료 송금
        Rent rent = createRent(chatRoom, chatRoom.getItem(), fee); // 대여 정보 생성
        saveRedisKey(rent.getId(), roomId, DEFAULT_REDIS_EXPIRATION_HOURS, TimeUnit.HOURS);
        chatMessageService.sendRentRequestMessage(renter, roomId); // 대여 요청 메시지 전송

        return rent.getId();
    }

    // 대여 수락하기
    @Override
    @Transactional
    public Long acceptRent(Member owner, Long roomId, Long rentId, LocalDateTime endDate) {
        ChatRoom chatRoom = getValidatedChatRoomForOwner(owner, roomId);
        validateRentIdInRedis(roomId, rentId);

        Rent rent = getRent(rentId);

        changeRentStatus(rent, LocalDateTime.now(), endDate, RentStatus.ACCEPT); // 대여 정보 변경
        chatRoom.getItem().setStatus(Item.Status.RENTED); // 물품 상태 변경

        accountService.receiveTransfer(
                owner, rent.getRentFee() + chatRoom.getItem().getDeposit()); // 대여료 입금 처리

        saveRedisKey(rentId, roomId, endDate); // Redis에 대여 정보 저장, 만료 날짜 설정
        itemRepository.save(chatRoom.getItem());

        chatMessageService.sendRentAcceptMessage(owner, roomId); // 대여 수락 메시지 전송
        return rent.getId();
    }

    // 대여 정보 검증
    @Override
    public Rent getRent(Long rentId) {
        return rentRepository
                .findById(rentId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.RENT_NOT_EXIST));
    }

    // 대여 정보 생성
    @Override
    @Transactional
    public Rent createRent(ChatRoom chatRoom, Item item, Long fee) {
        Rent rent = RentConverter.toRent(chatRoom, item, fee);
        item.addRent(rent);
        return rentRepository.save(rent);
    }

    @Override
    @Transactional
    public void changeRentStatus(
            Rent rent, LocalDateTime startDate, LocalDateTime endDate, RentStatus status) {

        rent.setStartDate(startDate);
        rent.setEndDate(endDate);
        rent.setStatus(status);

        rentRepository.save(rent);
    }

    // Redis에서 대여 정보 확인
    private void validateRentIdInRedis(Long roomId, Long rentId) {
        String rentIdInRedisStr = (String) redisTemplate.opsForValue().get(RENT + roomId);
        Long rentIdInRedis = rentIdInRedisStr == null ? null : Long.parseLong(rentIdInRedisStr);

        if (rentIdInRedis == null || !rentIdInRedis.equals(rentId)) {
            throw new GlobalException(GlobalErrorCode.RENT_NOT_EXIST);
        }
    }

    // Redis에 대여 정보 저장
    private void saveRedisKey(Long rentId, Long roomId, int duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(RENT + roomId, rentId, duration, timeUnit);
    }

    private void saveRedisKey(Long rentId, Long roomId, LocalDateTime endDate) {
        long expirationInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), endDate);
        redisTemplate.opsForValue().set(RENT + roomId, rentId, expirationInSeconds, TimeUnit.SECONDS);
    }

    // 대여자인지 확인 후 ChatRoom 반환
    private ChatRoom getValidatedChatRoomForRenter(Member renter, Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        validateRenter(renter, chatRoom);
        checkRentProgress(chatRoom);
        checkItemAvailability(chatRoom);
        return chatRoom;
    }

    // 소유자인지 확인 후 ChatRoom 반환
    private ChatRoom getValidatedChatRoomForOwner(Member owner, Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        validateOwner(owner, chatRoom);
        checkItemAvailability(chatRoom);
        return chatRoom;
    }

    // 대여 진행 상황 확인
    private void checkRentProgress(ChatRoom chatRoom) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(RENT + chatRoom.getId()))) {
            throw new GlobalException(GlobalErrorCode.RENT_IN_PROGRESS);
        }
    }

    // 대여 가능 여부 확인
    private void checkItemAvailability(ChatRoom chatRoom) {
        if (!chatRoom.getItem().getStatus().equals(Item.Status.AVAILABLE)) {
            throw new GlobalException(GlobalErrorCode.ITEM_NOT_AVAILABLE);
        }
    }

    // 대여자인지 확인
    private void validateRenter(Member renter, ChatRoom chatRoom) {
        if (!chatRoom.getRenter().getId().equals(renter.getId())) {
            throw new GlobalException(GlobalErrorCode.NOT_RENTER);
        }
    }

    // 소유자인지 확인
    private void validateOwner(Member owner, ChatRoom chatRoom) {
        if (!chatRoom.getOwner().getId().equals(owner.getId())) {
            throw new GlobalException(GlobalErrorCode.NOT_OWNER);
        }
    }
}
