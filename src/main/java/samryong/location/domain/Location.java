package samryong.location.domain;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import samryong.member.domain.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

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
    private List<Member> members;
}
