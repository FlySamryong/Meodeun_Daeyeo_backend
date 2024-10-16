package samryong.domain.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.item.entity.Item;
import samryong.domain.member.entity.Member;
import samryong.global.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "city", columnDefinition = "varchar(30)")
    private String city;

    @Column(name = "district", columnDefinition = "varchar(30)")
    private String district;

    @Column(name = "neighborhood", columnDefinition = "varchar(40)")
    private String neighborhood;

    @OneToMany(mappedBy = "location")
    private List<Member> memberList;

    @OneToMany(mappedBy = "location")
    private List<Item> itemList;
}
