package samryong.domain.rent.converter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.location.converter.LocationConverter;
import samryong.domain.rent.dto.RentDTO;
import samryong.domain.rent.dto.RentDTO.MyRentOrLoanResponseListDTO;
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

    public static MyRentOrLoanResponseListDTO toMyRentOrLoanResponseListDTO(List<Rent> rentList) {
        return RentDTO.MyRentOrLoanResponseListDTO.builder()
                .rentOrLoanList(
                        rentList.stream()
                                // 연체 및 대여 진행 중 상태만 필터링
                                .filter(
                                        rent ->
                                                rent.getStatus() == Rent.RentStatus.OVERDUE
                                                        || rent.getStatus() == Rent.RentStatus.RENT_PROCESS)
                                // startDate와 endDate가 null이 아닌 데이터만 필터링
                                .filter(rent -> rent.getStartDate() != null && rent.getEndDate() != null)
                                .map(RentConverter::toRentResponseDTO)
                                .collect(Collectors.toList()))
                .build();
    }

    public static RentDTO.RentResponseDTO toRentResponseDTO(Rent rent) {
        // 날짜 및 시간 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // StartDate와 EndDate를 포맷팅
        String period =
                rent.getStartDate().format(formatter) + " ~ " + rent.getEndDate().format(formatter);

        return RentDTO.RentResponseDTO.builder()
                .id(rent.getId())
                .itemName(rent.getItem().getName())
                .period(period)
                .location(LocationConverter.toLocationResponseDTO(rent.getItem().getLocation()))
                .status(rent.getStatus().name())
                .build();
    }
}
