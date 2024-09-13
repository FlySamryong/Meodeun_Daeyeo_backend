package samryong.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private String id; // MongoBDB ID는 스트링으로 설정

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
