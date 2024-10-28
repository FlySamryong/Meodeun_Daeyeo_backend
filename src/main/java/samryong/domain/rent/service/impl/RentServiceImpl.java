package samryong.domain.rent.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.service.ChatRoomService;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.converter.RentConverter;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;
import samryong.domain.rent.repository.RentRepository;
import samryong.domain.rent.service.RentService;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final ChatRoomService chatRoomService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String RENT = "RENT:";

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
        Long overDueFee = (long) (item.getDeposit() * 0.1);

        Rent rent = RentConverter.toRent(chatRoom, fee, overDueFee, RentStatus.REQUEST);
        item.addRent(rent);

        return rentRepository.save(rent);
    }

    // 대여 정보 삭제
    @Override
    @Transactional
    public void deleteRent(Rent rent) {
        Item item = rent.getItem();
        item.removeRent(rent);

        rentRepository.delete(rent);
    }

    @Override
    @Transactional
    public void changeRentStatus(
            Rent rent, LocalDateTime startDate, LocalDateTime endDate, RentStatus status) {
        if (startDate != null) {
            rent.setStartDate(startDate);
        }
        if (endDate != null) {
            rent.setEndDate(endDate);
        }
        rent.setStatus(status);
        rentRepository.save(rent);
    }

    @Override
    public void validateRentIdInRedis(Long roomId, Long rentId) {
        String value = (String) redisTemplate.opsForValue().get(RENT + rentId);
        Long id = value == null ? null : Long.parseLong(value);

        if (id == null || !id.equals(roomId)) {
            throw new GlobalException(GlobalErrorCode.RENT_NOT_EXIST);
        }
    }

    @Override
    public void saveRentKey(Long rentId, Long roomId, int duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(RENT + rentId, roomId, duration, timeUnit);
    }

    @Override
    public void saveRentKey(Long rentId, Long roomId, LocalDateTime endDate) {
        long expirationInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), endDate);
        redisTemplate.opsForValue().set(RENT + rentId, roomId, expirationInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public ChatRoom getValidatedChatRoomForRenter(Member renter, Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        validateRenter(renter, chatRoom);
        return chatRoom;
    }

    @Override
    public ChatRoom getValidatedChatRoomForOwner(Member owner, Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        validateOwner(owner, chatRoom);
        return chatRoom;
    }

    @Override
    public void checkItemAvailability(Item item) {
        if (!item.getStatus().equals(Item.Status.AVAILABLE)) {
            throw new GlobalException(GlobalErrorCode.ITEM_NOT_AVAILABLE);
        }
    }

    @Override
    public void validateRenter(Member renter, ChatRoom chatRoom) {
        if (!chatRoom.getRenter().getId().equals(renter.getId())) {
            throw new GlobalException(GlobalErrorCode.NOT_RENTER);
        }
    }

    @Override
    public void validateOwner(Member owner, ChatRoom chatRoom) {
        if (!chatRoom.getOwner().getId().equals(owner.getId())) {
            throw new GlobalException(GlobalErrorCode.NOT_OWNER);
        }
    }
}
