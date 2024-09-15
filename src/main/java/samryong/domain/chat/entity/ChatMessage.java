package samryong.domain.chat.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
public class ChatMessage {

    @Id private String id; // MongoBDB ID는 스트링으로 설정

    private Long chatRoomId; // 채팅방 ID

    private Long senderId; // 보낸 사람 ID

    private String message; // 메시지 텍스트

    private String imageUri; // 사진

    private LocalDateTime createdAt; // 메시지 생성 시간

    @Enumerated(EnumType.STRING)
    private ChatType type;

    public enum ChatType {
        TEXT, // 일반 텍스트 메시지
        IMAGE, // 이미지 메시지
        RENT_REQ, // 물품 대여 요청
        RENT_RES, // 물품 대여 응답
        RET_REQ, // 물품 반납 요청
        RET_RES, // 물품 반납 응답
        DEPOSIT_RES, // 보증금 요청
    }
}
