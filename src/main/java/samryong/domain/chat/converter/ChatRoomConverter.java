package samryong.domain.chat.converter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;
import samryong.domain.chat.dto.ChatRoomDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomHashDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomListResponseDTO;
import samryong.domain.chat.dto.ChatRoomDTO.ChatRoomResponseDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;

@Component
public class ChatRoomConverter {

    public static ChatRoom toChatRoom(Member renter, Member owner, Item item) {
        return ChatRoom.builder().renter(renter).owner(owner).item(item).build();
    }

    public static ChatRoomResponseDTO toChatRoomResponseDTO(
            ChatRoom chatRoom, ChatMessage lastMessage) {
        return ChatRoomDTO.ChatRoomResponseDTO.builder()
                .chatRoomId(chatRoom.getId())
                .ownerName(chatRoom.getOwner().getNickName())
                .renterName(chatRoom.getRenter().getNickName())
                .itemName(chatRoom.getItem().getName())
                .updatedDate(
                        chatRoom.getUpdatedDate() != null
                                ? chatRoom
                                        .getUpdatedDate()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                : "날짜 없음") // 기본값 설정
                .lastMessage(lastMessage != null ? lastMessage.getMessage() : "메시지 없음") // 기본값 설정
                .build();
    }

    public static ChatRoomListResponseDTO toChatRoomListResponseDTO(
            List<ChatRoomResponseDTO> chatRoomList) {
        return ChatRoomListResponseDTO.builder().chatRoomList(chatRoomList).build();
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
