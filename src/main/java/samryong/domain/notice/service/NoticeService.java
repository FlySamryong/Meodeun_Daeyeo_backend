package samryong.domain.notice.service;

import samryong.domain.rent.entity.Rent;

public interface NoticeService {
    void dailyRemind();

    void tradeRemind(Rent rent);
}
