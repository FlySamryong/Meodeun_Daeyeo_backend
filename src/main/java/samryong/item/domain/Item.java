package samryong.item.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import samryong.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column() private String name;

    @Column() private String description;

    @Column() private String status;

    private LocalDateTime created_at;

    @Column(name = "rental_period")
    private Long period;

    @Column(name = "rental_fee")
    private Long fee;

    @Column() private Long deposit;

    @Column(name = "image_url")
    private String image;

    @Column() private Enum type;

    // Location이 구현된 후 주석해제
    /*
    @JoinColumn(name = "location_id")
    private Location location;
    */
}
