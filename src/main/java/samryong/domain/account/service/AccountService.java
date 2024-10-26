package samryong.domain.account.service;

import samryong.domain.account.entity.Account;
import samryong.domain.member.entity.Member;

public interface AccountService {

    void drawTransfer(Member sender, Member receiver, Long amount);

    void receiveTransfer(Member receiver, Long amount);

    Account getAccount(Member member);
}
