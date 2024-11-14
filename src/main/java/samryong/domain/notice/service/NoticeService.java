package samryong.domain.notice.service;

public interface NoticeService {
    void dailyRemind();

    void tradeRemind(Long rentId);
}
