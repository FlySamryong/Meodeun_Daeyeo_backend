package samryong.domain.chat.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRenterAndOwnerAndItem(Member renter, Member owner, Item item);

    List<ChatRoom> findAllByRenterOrOwner(Member renter, Member owner, Sort sort);
}
