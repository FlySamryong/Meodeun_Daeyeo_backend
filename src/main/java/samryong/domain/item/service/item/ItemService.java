package samryong.domain.item.service.item;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import samryong.domain.item.dto.ItemDTO.ItemListRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewListResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemResponseDTO;
import samryong.domain.item.dto.ItemDTO.WishListResponseDTO;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.member.entity.Member;

public interface ItemService {

    Long createItem(
            Member member,
            ItemRequestDTO itemDTO,
            LocationRequestDTO locationDTO,
            List<MultipartFile> imageList);

    ItemResponseDTO getItemDetail(Long itemId, Member member);

    ItemPreviewListResponseDTO searchItem(ItemListRequestDTO requestDTO, int page);

    WishListResponseDTO getWishList(Member member);
}
