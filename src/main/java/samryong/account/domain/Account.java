package samryong.account.domain;
import jakarta.persistence.*;
import lombok.*;
import samryong.member.domain.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "refresh_token")
    private String refresh_token;

    @Column(name = "fin_tec_use_num")
    private String fin_tec_use_num;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
