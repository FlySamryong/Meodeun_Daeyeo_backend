package samryong.domain.item.service.item;

import samryong.domain.item.dto.ItemRegistrationDTO;

public interface ItemService {
    public Long createItem(ItemRegistrationDTO.ItemRequestDTO requestDto);

    public ItemRegistrationDTO.ItemResponseDTO getItem(Long Id);
}
