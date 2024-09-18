package samryong.domain.chat.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findTop100ByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
}
