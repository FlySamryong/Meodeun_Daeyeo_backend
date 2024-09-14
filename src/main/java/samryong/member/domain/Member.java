package samryong.member.domain;

import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.account.domain.Account;
import samryong.chat.domain.ChatRoom;
import samryong.location.domain.Location;
import samryong.rent.domain.Rent;
import samryong.report.domain.Report;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(columnDefinition = "varchar(30)")
    private String nickName;

    @Column(columnDefinition = "varchar(50)")
    private String email;

    // 매너온도
    @Column(name = "manner_rate", columnDefinition = "Long")
    private Long manner_rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Account bankInfo;

    // 신고자가 만든 신고 목록
    @OneToMany(mappedBy = "reporter")
    private List<Report> reports;

    // 신고받은 신고 목록
    @OneToMany(mappedBy = "reported")
    private List<Report> reportsReceived;

    // 빌린 리스트
    @OneToMany(mappedBy = "rent_item")
    private List<Rent> rentList;

    // 빌려준 리스트
    @OneToMany(mappedBy = "loan_item")
    private List<Rent> loanList;

    @OneToMany(mappedBy = "chatroom")
    private List<ChatRoom> chatRooms;
}
