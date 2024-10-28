package samryong.domain.rent.service;

import samryong.domain.member.entity.Member;

public interface RentReturnService {

    String generateOTP(Member owner, Long roomId, Long rentId);

    void verifyOTP(Member renter, Long roomId, Long rentId, String otp);

    void returnDeposit(Member owner, Long roomId, Long rentId, Boolean isReturn);
}
