package samryong.rent.domain;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samryong.global.BaseEntity;
import samryong.member.domain.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private Member renter; // 대여자

    private LocalDateTime startDate; // 대여 시작일

    private LocalDateTime endDate; // 대여 종료일

    private Long overDueFee; // 연체료

    @Enumerated(EnumType.STRING)
    private RentStatus status; // 대여 상태

    public enum RentStatus {
        REQUEST, // 대여 요청
        ACCEPT, // 대여 승인
        RENT_PROCESS, // 대여 진행
        RETURN_REQUEST, // 반납 요청
        RETURN_ACCEPT, // 반납 승인
        OVERDUE, // 연체
    }
}
