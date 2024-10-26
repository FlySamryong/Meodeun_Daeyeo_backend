package samryong.domain.rent.service;

import java.time.LocalDateTime;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;

public interface RentService {

    // 대여료 송금하기
    Long sendRentFee(Member renter, Long roomId, Long fee);

    // 대여 수락하기
    Long acceptRent(Member owner, Long roomId, Long rentId, LocalDateTime endDate);

    Rent createRent(ChatRoom chatRoom, Item item, Long fee);

    Rent getRent(Long rentId);

    void changeRentStatus(
            Rent rent, LocalDateTime startDate, LocalDateTime endDate, RentStatus status);
}
