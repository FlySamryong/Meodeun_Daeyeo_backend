package samryong.domain.chat.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatRoomRequestDTO {

        @NotNull(message = "물품 ID는 필수 입력 값입니다.")
        private Long itemId; // 물품 ID

        @NotNull(message = "물품 등록자 ID는 필수 입력 값입니다.")
        private Long ownerId; // 물품 등록자 ID
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatRoomHashDTO implements Serializable {

        private static final long serialVersionUID = 6494678977089006639L;
        private Long chatRoomId; // 채팅방 ID
        private Long itemId; // 물품 ID
        private Long ownerId; // 물품 등록자 ID
        private Long renterId; // 물품 대여자 ID
    }
}
