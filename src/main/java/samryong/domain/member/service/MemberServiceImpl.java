package samryong.domain.member.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.converter.AccountConverter;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountRequestDTO;
import samryong.domain.account.dto.NonghyupAccountDTO.NonghyupAccountResponseDTO;
import samryong.domain.account.entity.Account;
import samryong.domain.account.repository.AccountRepository;
import samryong.domain.bank.nonghyup.provider.NonghyupTransactionProvider;
import samryong.domain.item.converter.WishItemConverter;
import samryong.domain.item.entity.WishItem;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.dto.LocationDTO.LocationResponseDTO;
import samryong.domain.location.entity.Location;
import samryong.domain.location.repository.LocationRepository;
import samryong.domain.member.converter.MemberConverter;
import samryong.domain.member.dto.MemberDTO.MyInformationResponseDTO;
import samryong.domain.member.entity.Member;
import samryong.domain.member.repository.MemberRepository;
import samryong.domain.rent.converter.RentConverter;
import samryong.domain.rent.dto.RentDTO.MyRentOrLoanResponseListDTO;
import samryong.domain.rent.entity.Rent;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final AccountRepository accountRepository;
    private final NonghyupTransactionProvider nonghyupTransactionProvider;
    private final ItemRepository itemRepository;

    private final String AlreadyExistAccount = "A0013";

    @Override
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    @Transactional
    public NonghyupAccountResponseDTO registerAccount(
            Member member, NonghyupAccountRequestDTO requestDTO) {
        String accountNumber = requestDTO.getAccountNumber();

        // 농협에 계좌 등록 요청 및 처리
        String rgNum = nonghyupTransactionProvider.openFinAccountDirect(accountNumber);
        String finTechAccountNum = getFinTechAccountNum(accountNumber, rgNum);

        // 계좌 정보를 조회하거나 새로 생성
        Account account =
                accountRepository
                        .findByAccountNum(accountNumber)
                        .orElseGet(() -> createNewAccount(member, accountNumber, finTechAccountNum));

        return AccountConverter.toAccountResponseDTO(account);
    }

    private String getFinTechAccountNum(String accountNumber, String rgNum) {
        if (rgNum.equals(AlreadyExistAccount)) {
            return accountRepository
                    .findByAccountNum(accountNumber)
                    .map(Account::getFinTechAccountNum)
                    .orElseThrow(() -> new GlobalException(GlobalErrorCode.ACCOUNT_NOT_FOUND));
        }
        return nonghyupTransactionProvider.checkOpenFinAccountDirect(rgNum);
    }

    private Account createNewAccount(Member member, String accountNumber, String finTechAccountNum) {
        Account account = AccountConverter.toAccount(accountNumber, finTechAccountNum);
        member.addAccount(account);
        return account;
    }

    @Override
    @Transactional
    public LocationResponseDTO registerLocation(Member member, LocationRequestDTO requestDTO) {

        String City = requestDTO.getCity();
        String District = requestDTO.getDistrict();
        String Neighborhood = requestDTO.getNeighborhood();

        Location location =
                locationRepository
                        .findByCityAndDistrictAndNeighborhood(City, District, Neighborhood)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.LOCATION_NOT_FOUND));

        member.setLocation(location);

        return LocationConverter.toLocationResponseDTO(location);
    }

    @Override
    public MyInformationResponseDTO getMyPage(Long memberId) {

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND));

        return MemberConverter.toMemberResponseDTO(member);
    }

    @Override
    @Transactional
    public void updateMannerRate(Member member, Long mannerRate) {
        double newMannerRate =
                (member.getMannerRate() * member.getMannerCount() + mannerRate)
                        / (member.getMannerCount() + 1);

        // 소수점 첫째 자리까지만 허용 (반올림)
        newMannerRate = Math.round(newMannerRate * 10) / 10.0;

        member.setMannerRate(newMannerRate, member.getMannerCount() + 1);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateRentList(Member member, Rent rent) {
        member.addRent(rent);
    }

    @Override
    @Transactional
    public void updateLoanList(Member member, Rent rent) {
        member.addLoan(rent);
    }

    @Override
    public MyRentOrLoanResponseListDTO getMyRentOrLoanList(Member member) {
        List<Rent> rentList = member.getRentList();
        List<Rent> loanList = member.getLoanList();
        List<Rent> rentOrLoanList = new ArrayList<>();

        rentOrLoanList.addAll(rentList);
        rentOrLoanList.addAll(loanList);

        rentOrLoanList.sort(Comparator.comparing(Rent::getCreatedAt).reversed());

        return RentConverter.toMyRentOrLoanResponseListDTO(rentOrLoanList);
    }

    @Override
    @Transactional
    public void updateWishItemList(Member member, Long itemId) {
        WishItem wishItem =
                WishItemConverter.toWishItem(
                        member,
                        itemRepository
                                .findById(itemId)
                                .orElseThrow(() -> new GlobalException(GlobalErrorCode.ITEM_NOT_FOUND)));
        member.addWishItemList(wishItem);
        memberRepository.save(member);
    }
}
