package samryong.domain.rent.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;

public interface RentService {

    Rent createRent(ChatRoom chatRoom, Item item, Long fee);

    Rent getRent(Long rentId);

    void changeRentStatus(
            Rent rent, LocalDateTime startDate, LocalDateTime endDate, RentStatus status);

    void validateRentIdInRedis(Long roomId, Long rentId);

    // Redis에 대여 정보 저장
    void saveRentKey(Long rentId, Long roomId, int duration, TimeUnit timeUnit);

    void saveRentKey(Long rentId, Long roomId, LocalDateTime endDate);

    // 대여자인지 확인 후 ChatRoom 반환
    ChatRoom getValidatedChatRoomForRenter(Member renter, Long roomId);

    // 소유자인지 확인 후 ChatRoom 반환
    ChatRoom getValidatedChatRoomForOwner(Member owner, Long roomId);

    // 대여 가능 여부 확인
    void checkItemAvailability(Item item);

    // 대여자인지 확인
    void validateRenter(Member renter, ChatRoom chatRoom);

    // 소유자인지 확인
    void validateOwner(Member owner, ChatRoom chatRoom);

    void deleteRent(Rent rent);
}
