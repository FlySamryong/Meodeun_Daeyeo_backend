package samryong.domain.rent.service.impl;

import static samryong.domain.chat.entity.ChatMessage.ChatType.*;
import static samryong.global.code.GlobalErrorCode.*;

import jakarta.transaction.Transactional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.account.service.AccountService;
import samryong.domain.chat.entity.ChatRoom;
import samryong.domain.chat.repository.ChatRoomRepository;
import samryong.domain.chat.service.ChatMessageService;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.Item.Status;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.member.entity.Member;
import samryong.domain.notice.service.NoticeService;
import samryong.domain.rent.entity.Rent;
import samryong.domain.rent.entity.Rent.RentStatus;
import samryong.domain.rent.service.RentExpirationService;
import samryong.domain.rent.service.RentService;
import samryong.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class RentExpirationServiceImpl implements RentExpirationService {

    private final RentService rentService;
    private final AccountService accountService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final ItemRepository itemRepository;
    private final NoticeService noticeService;

    private static final String RENT = "RENT:";
    private static final int DEFAULT_OVERDUE_HOURS = 24; // 다음 연체료 출금까지 기간
    private static final String RENT_REQUEST_EXPIRATION_MESSAGE = "대여 요청이 수락되지 않아 대여가 취소되었습니다.";
    private static final String RENT_ACCEPT_EXPIRATION_MESSAGE = "대여 요청을 최종적으로 확인하지 않아 대여가 취소되었습니다.";
    private static final String OVERDUE_MESSAGE = "물품 반납이 지연되어 연체료가 부과되었습니다. 24시간 내로 반납해주세요.";

    @Override
    @Transactional
    public void processRentKeyExpiration(String expiredKey) {

        // 1. Rent 정보 조회
        Rent rent = rentService.getRent(extractId(expiredKey, RENT));

        // 2. Rent 상태 확인 후 각 상태에 따른 처리
        switch (rent.getStatus()) {
            case REQUEST:
                processRentRequestExpiration(rent, RentStatus.REQUEST);
                break;
            case ACCEPT:
                processRentRequestExpiration(rent, RentStatus.ACCEPT);
                break;
            case RENT_PROCESS, OVERDUE:
                processRentReturnExpiration(rent);
                break;
            case NOTICE:
                noticeService.tradeRemind(rent);
                break;
            default:
        }
    }

    @Override
    @Transactional
    public void processRentRequestExpiration(Rent rent, RentStatus status) {

        Member renter = rent.getRenter();
        Member owner = rent.getOwner();
        Item item = rent.getItem();
        ChatRoom chatRoom =
                chatRoomRepository
                        .findByRenterAndOwnerAndItem(renter, owner, item)
                        .orElseThrow(() -> new GlobalException(CHAT_ROOM_NOT_FOUND));
        Long roomId = chatRoom.getId();

        // 1. 대여료 및 보증금 반환
        accountService.receiveTransfer(renter, rent.getRentFee() + item.getDeposit());

        // 2. Rent 정보 삭제
        rentService.deleteRent(rent);

        // 3. 물품 상태 변경
        item.setStatus(Status.AVAILABLE);
        itemRepository.save(item);

        // 3. 대여 정보 메시지 전송
        if (status == RentStatus.REQUEST) {
            chatMessageService.sendRentActivityMessage(
                    owner, roomId, RENT_REQUEST_EXPIRATION_MESSAGE, CANCEL);
        } else if (status == RentStatus.ACCEPT) {
            chatMessageService.sendRentActivityMessage(
                    renter, roomId, RENT_ACCEPT_EXPIRATION_MESSAGE, CANCEL);
        }
    }

    @Override
    @Transactional
    public void processRentReturnExpiration(Rent rent) {

        Long overdueFee = rent.getOverDueFee();
        Member renter = rent.getRenter();
        Member owner = rent.getOwner();
        ChatRoom chatRoom =
                chatRoomRepository
                        .findByRenterAndOwnerAndItem(renter, owner, rent.getItem())
                        .orElseThrow(() -> new GlobalException(CHAT_ROOM_NOT_FOUND));
        Long roomId = chatRoom.getId();

        // 1. Rent 정보 변경
        if (rent.getStatus() == RentStatus.RENT_PROCESS) {
            rentService.changeRentStatus(rent, null, null, RentStatus.OVERDUE);
        }

        // 2. 연체료 부과
        accountService.drawTransfer(renter, owner, overdueFee);
        accountService.receiveTransfer(owner, overdueFee);

        // 3. Redis에 대여 정보 갱신
        rentService.saveRentKey(rent.getId(), roomId, DEFAULT_OVERDUE_HOURS, TimeUnit.HOURS);

        // 4. 연체료 메시지 전송
        chatMessageService.sendRentActivityMessage(owner, roomId, OVERDUE_MESSAGE, OVERDUE);
    }

    private Long extractId(String expiredKey, String prefix) {
        return Long.parseLong(expiredKey.substring(prefix.length()));
    }
}
