package samryong.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.member.domain.Member;
import samryong.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }
}
