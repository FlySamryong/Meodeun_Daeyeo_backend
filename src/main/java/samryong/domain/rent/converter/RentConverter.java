package samryong.domain.rent.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.member.entity.Member;
import samryong.domain.rent.dto.RentDTO;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;

@Component
public class RentConverter {

    public static Rent toRent(ChatRoom chatRoom, Long fee, Long overDueFee, RentStatus status) {
        return Rent.builder()
                .owner(chatRoom.getOwner())
                .renter(chatRoom.getRenter())
                .rentFee(fee)
                .overDueFee(overDueFee)
                .status(status)
                .build();
    }

    public static List<RentDTO.RentResponseDTO> toRentResponseDTOList(Member member) {
        return member.getRentList().stream()
                .map(
                        rent ->
                                RentDTO.RentResponseDTO.builder()
                                        .rentId(rent.getId())
                                        .itemName(rent.getItem().getName()) // 아이템 이름 가져오기
                                        .renterName(rent.getRenter().getNickName()) // 대여자 이름
                                        .ownerName(rent.getOwner().getNickName()) // 소유자 이름
                                        .startDate(rent.getStartDate())
                                        .endDate(rent.getEndDate())
                                        .rentFee(rent.getRentFee())
                                        .overDueFee(rent.getOverDueFee())
                                        .status(rent.getStatus().name()) // 상태 Enum의 이름
                                        .build())
                .collect(Collectors.toList());
    }

    public static List<RentDTO.LoanResponseDTO> toLoanResponseDTOList(Member member) {
        return member.getLoanList().stream()
                .map(
                        loan ->
                                RentDTO.LoanResponseDTO.builder()
                                        .rentId(loan.getId())
                                        .itemName(loan.getItem().getName()) // 아이템 이름 가져오기
                                        .renterName(loan.getRenter().getNickName()) // 대여자 이름
                                        .ownerName(loan.getOwner().getNickName()) // 소유자 이름
                                        .startDate(loan.getStartDate())
                                        .endDate(loan.getEndDate())
                                        .rentFee(loan.getRentFee())
                                        .overDueFee(loan.getOverDueFee())
                                        .status(loan.getStatus().name()) // 상태 Enum의 이름
                                        .build())
                .collect(Collectors.toList());
    }
}
