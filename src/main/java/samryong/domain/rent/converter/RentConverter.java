package samryong.domain.rent.converter;

import org.springframework.stereotype.Component;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;

@Component
public class RentConverter {

    public static Rent toRent(ChatRoom chatRoom, Long fee, Long overDueFee, RentStatus status) {
        return Rent.builder()
                .owner(chatRoom.getOwner())
                .renter(chatRoom.getRenter())
                .rentFee(fee)
                .overDueFee(overDueFee)
                .status(status)
                .build();
    }
}
