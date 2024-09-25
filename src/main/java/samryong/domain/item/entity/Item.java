package samryong.domain.item.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.image.Image;
import samryong.domain.location.entity.Location;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.entity.Rent;
import samryong.global.BaseEntity;

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

    @Enumerated(EnumType.STRING)
    private Status status; // 상품 상태

    @Column(name = "rental_period")
    private Long period; // 대여 가능 기간

    @Column(name = "rental_fee")
    private Long fee; // 대여료

    @Column(name = "rental_deposit")
    private Long deposit; // 보증금

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> imageList; // 상품 이미지 목록

    @OneToMany(mappedBy = "item")
    private List<ChatRoom> chatRoomsList; // 채팅방 목록

    @OneToMany(mappedBy = "item")
    private List<Rent> rentList; // 대여 목록

    @OneToMany(mappedBy = "item")
    private List<ItemCategory> itemCategoryList; // 상품 카테고리 목록

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public enum Status {
        RENTED, // 대여 중
        UNAVAILABLE, // 대여 불가
        AVAILABLE // 대여 가능
    }

    public void addItemCategory(ItemCategory itemCategory) {
        itemCategory.setItem(this);
        if (this.itemCategoryList == null) this.itemCategoryList = new ArrayList<>();
        this.itemCategoryList.add(itemCategory);
    }

    public void addImage(Image image) {
        image.setItem(this);
        if (this.imageList == null) this.imageList = new ArrayList<>();
        this.imageList.add(image);
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
