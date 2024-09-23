package samryong.domain.item.service.item;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import samryong.domain.item.dto.ItemRegistrationDTO;
import samryong.domain.item.entity.Item;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.location.entity.Location;
import samryong.domain.location.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;

    @Override
    public Long createItem(ItemRegistrationDTO.ItemRequestDTO requestDto) {
        Location location =
                locationRepository
                        .findById(requestDto.getLocationId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid location id"));
        Item item =
                Item.builder()
                        .name(requestDto.getName())
                        .description(requestDto.getDescription())
                        .period(requestDto.getPeriod())
                        .fee(requestDto.getFee())
                        .deposit(requestDto.getDeposit())
                        .imageUrl(requestDto.getImageUrl())
                        .location(location)
                        .status(Item.Status.AVAILABLE)
                        .build();
        itemRepository.save(item);
        return item.getId();
    }

    @Override
    public ItemRegistrationDTO.ItemResponseDTO getItem(Long Id) {
        Item item =
                itemRepository
                        .findById((Id))
                        .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        return new ItemRegistrationDTO.ItemResponseDTO();
    }
}
