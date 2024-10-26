package samryong.domain.rent.converter;

import org.springframework.stereotype.Component;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.rent.entity.Rent;

@Component
public class RentConverter {

    public static Rent toRent(ChatRoom chatRoom, Item item, Long fee) {
        return Rent.builder()
                .owner(chatRoom.getOwner())
                .renter(chatRoom.getRenter())
                .rentFee(fee)
                .overDueFee((long) (item.getDeposit() * 0.1))
                .status(Rent.RentStatus.REQUEST)
                .build();
    }
}
