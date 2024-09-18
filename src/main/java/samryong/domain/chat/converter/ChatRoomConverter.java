package samryong.domain.chat.converter;

import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomHashDTO;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;

@Component
public class ChatRoomConverter {

    public static ChatRoom toChatRoom(Member renter, Member owner, Item item) {
        return ChatRoom.builder().renter(renter).owner(owner).item(item).build();
    }

    public static ChatRoomHashDTO toChatRoomHashDTO(ChatRoom chatRoom) {
        return ChatRoomHashDTO.builder()
                .chatRoomId(chatRoom.getId())
                .itemId(chatRoom.getItem().getId())
                .ownerId(chatRoom.getOwner().getId())
                .renterId(chatRoom.getRenter().getId())
                .build();
    }
}
