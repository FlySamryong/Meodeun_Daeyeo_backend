package samryong.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.member.entity.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 상품 등록자

    @Column(columnDefinition = "varchar(50)")
    private String name; // 상품명

    @Column(columnDefinition = "varchar(255)")
    private String description; // 상품 설명

    private Status status; // 상품 상태

    @Column(name = "rental_period")
    private Long period; // 대여 가능 기간

    @Column(name = "rental_fee")
    private Long fee; // 대여료

    @Column(name = "rental_deposit")
    private Long deposit; // 보증금

    @Column(name = "image_url")
    private String imageUrl; // 이미지 URL

    @OneToMany(mappedBy = "item")
    private List<ChatRoom> chatRoomsList; // 채팅방 목록

    @OneToMany(mappedBy = "item")
    private List<Rent> rentList; // 대여 목록

    @OneToMany(mappedBy = "item")
    private List<ItemCategory> itemCategoryList; // 상품 카테고리 목록

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public enum Status {
        RENTED, // 대여 중
        UNAVAILABLE, // 대여 불가
        AVAILABLE // 대여 가능
    }
}
