package samryong.domain.rent.service;

import java.time.LocalDateTime;
import samryong.domain.member.entity.Member;

public interface RentRequestService {

    // 대여료 송금하기
    Long sendRentFee(Member renter, Long roomId, Long fee);

    // 대여 수락하기
    Long acceptRent(Member owner, Long roomId, Long rentId, LocalDateTime endDate);

    // 최종 대여 동의, 대여가 진행됨
    Long acceptRentProcess(Member renter, Long roomId, Long rentId);
}
