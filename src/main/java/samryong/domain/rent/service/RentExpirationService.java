package samryong.domain.rent.service;

import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;

public interface RentExpirationService {

    void processRentKeyExpiration(String expiredKey);

    // 대여 요청이 이루어진 후 유효 기간 내에 대여가 성립이 안된 경우
    void processRentRequestExpiration(Rent rent, RentStatus status);

    // 물품 반납이 유효 기간 내에 안 이루어진 경우
    void processRentReturnExpiration(Rent rent);
}
