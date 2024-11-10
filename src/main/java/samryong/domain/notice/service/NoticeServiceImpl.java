package samryong.domain.notice.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import samryong.domain.chat.dto.ChatMessageDTO;
import samryong.domain.chat.entity.ChatMessage;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.repository.ChatRoomRepository;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.repository.RentRepository;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final String DAILY_REMINDER_MESSAGE = "안녕하세요!\n오늘은 물품 반납일입니다. \n반납 준비를 미리 시작해 주세요. 😊";
    private final String REMINDER_MESSAGE =
            "안녕하세요!\n반납 예정 시간이 두 시간 남았습니다. \n반납 준비가 완료되었는지 확인해 주세요. 감사합니다! 🙏";

    private final RentRepository rentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

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
                        ChatMessageDTO.ChatMessageRequestDTO.builder()
                                .chatRoomId(chatRoom.getId())
                                .senderId(0L) // 관리자 ID 지정
                                .message(DAILY_REMINDER_MESSAGE)
                                .type(ChatMessage.ChatType.NOTICE)
                                .build());
            }
        }
    }

    @Override
    public void tradeRemind() {}
}
