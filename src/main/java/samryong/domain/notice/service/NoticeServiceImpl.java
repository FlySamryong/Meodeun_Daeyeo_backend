package samryong.domain.notice.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.repository.ChatRoomRepository;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.notice.converter.NoticeConverter;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;
import samryong.domain.rent.repository.RentRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final String DAILY_REMINDER_MESSAGE = "안녕하세요!\n오늘은 물품 반납일입니다.\n반납 준비를 미리 시작해 주세요. 😊";
    private final String RENT_REMINDER_MESSAGE =
            "안녕하세요!\n반납 예정 시간이 두 시간 남았습니다.\n반납 준비가 완료되었는지 확인해 주세요. 감사합니다! 🙏";

    private final RentRepository rentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    // 매일 오전 9시에 실행, 반납일자가 오늘이고 대여중인 경우 대여자에게 알림
    @Override
    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시에 실행
    public void dailyRemind() {
        List<Rent> rentList = rentRepository.findAll();
        for (Rent rent : rentList) {
            if (rent.getEndDate().toLocalDate().equals(LocalDate.now())
                    && (rent.getStatus() == Rent.RentStatus.RENT_PROCESS)) { // 반납일자가 오늘이고 대여중인 경우
                ChatRoom chatRoom =
                        chatRoomRepository
                                .findByRenterAndOwnerAndItem(rent.getRenter(), rent.getOwner(), rent.getItem())
                                .get();
                chatMessageService.publishMessage(
                        NoticeConverter.toChatMessageRequestDTO(chatRoom, DAILY_REMINDER_MESSAGE));
            }
        }
    }

    // 대여자에게 대여 종료 2시간 전 알림
    @Override
    public void tradeRemind(Long rentId) {
        Rent rent =
                rentRepository
                        .findById(rentId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.RENT_NOT_EXIST));
        ChatRoom chatRoom =
                chatRoomRepository
                        .findByRenterAndOwnerAndItem(rent.getRenter(), rent.getOwner(), rent.getItem())
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CHAT_ROOM_NOT_FOUND));

        if (rent.getStatus() == RentStatus.RENT_PROCESS || rent.getStatus() == RentStatus.OVERDUE) {
            chatMessageService.publishMessage(
                    NoticeConverter.toChatMessageRequestDTO(chatRoom, RENT_REMINDER_MESSAGE));
        }
    }
}
