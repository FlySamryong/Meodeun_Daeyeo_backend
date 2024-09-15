package samryong.domain.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.domain.account.entity.Account;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.location.entity.Location;
import samryong.domain.rent.entity.Rent;
import samryong.domain.report.entity.Report;
import samryong.global.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Account> accounts;

    // 신고자가 만든 신고 목록
    @OneToMany(mappedBy = "reporter")
    private List<Report> reports;

    // 신고받은 신고 목록
    @OneToMany(mappedBy = "reported")
    private List<Report> reportsReceived;

    // 빌린 리스트
    @OneToMany(mappedBy = "renter")
    private List<Rent> rentList;

    // 빌려준 리스트
    @OneToMany(mappedBy = "owner")
    private List<Rent> loanList;

    // 채팅방 리스트
    @OneToMany(mappedBy = "owner")
    private List<ChatRoom> ownerChatRooms;

    // 채팅방 리스트
    @OneToMany(mappedBy = "renter")
    private List<ChatRoom> renterChatRooms;
}
