package samryong.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.chat.entity.ChatMessage.ChatType;

public class ChatMessageDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageRequestDTO {

        @NotNull(message = "채팅방 ID는 필수 입력 값입니다.")
        private Long chatRoomId; // 채팅방 ID

        @NotNull(message = "보낸 사람 ID는 필수 입력 값입니다.")
        private Long senderId; // 보낸 사람 ID

        @NotBlank(message = "메시지 텍스트는 필수 입력 값입니다.")
        private String message; // 메시지 텍스트

        private String imageCode; // 사진, 추후 바이트 코드를 받아서 S3 업로드 후 URI를 저장하는 과정을 거쳐야 함

        @NotBlank(message = "채팅 타입은 필수 입력 값입니다.")
        private ChatType type;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageResponseDTO {

        private Long chatRoomId; // 채팅방 ID

        private Long senderId; // 보낸 사람 ID

        private String message; // 메시지 텍스트

        private String imageUri; // 사진

        private LocalDateTime createdAt; // 메시지 생성 시간

        private ChatType type;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMessageResponseListDTO {

        private List<ChatMessageResponseDTO> chatMessageList;
    }
}
