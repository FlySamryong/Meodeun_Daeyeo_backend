package samryong.domain.item.contorller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samryong.domain.item.dto.ItemRegistrationDTO;
import samryong.domain.item.service.item.ItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemRegistrationController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Long> createItem(
            @RequestBody ItemRegistrationDTO.ItemRequestDTO requestDTO) {
        Long itemId = itemService.createItem(requestDTO);
        return ResponseEntity.ok(itemId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRegistrationDTO.ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemRegistrationDTO.ItemResponseDTO item = itemService.getItem(id);
        return ResponseEntity.ok(item);
    }
}
