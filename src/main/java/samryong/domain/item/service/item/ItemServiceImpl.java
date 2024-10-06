package samryong.domain.item.service.item;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import samryong.domain.image.ImageConverter;
import samryong.domain.item.converter.ItemConverter;
import samryong.domain.item.document.ItemDocument;
import samryong.domain.item.dto.ItemDTO.ItemListRequestDTO;
import samryong.domain.item.dto.ItemDTO.ItemPreviewListResponseDTO;
import samryong.domain.item.dto.ItemDTO.ItemRequestDTO;
import samryong.domain.item.entity.Category;
import samryong.domain.item.entity.Item;
import samryong.domain.item.entity.ItemCategory;
import samryong.domain.item.repository.CategoryRepository;
import samryong.domain.item.repository.ItemCategoryRepository;
import samryong.domain.item.repository.ItemRepository;
import samryong.domain.item.repository.elastic.ItemElasticRepository;
import samryong.domain.item.service.category.CategoryService;
import samryong.domain.item.service.itemCategory.ItemCategoryService;
import samryong.domain.location.dto.LocationDTO.LocationRequestDTO;
import samryong.domain.location.entity.Location;
import samryong.domain.location.service.LocationService;
import samryong.domain.member.entity.Member;
import samryong.domain.uuid.Uuid;
import samryong.domain.uuid.UuidRepository;
import samryong.global.aws.AmazonS3Manager;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final ItemCategoryService itemCategoryService;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final ItemElasticRepository itemElasticRepository;

    @Override
    @Transactional
    public Long createItem(
            Member member,
            ItemRequestDTO itemDTO,
            LocationRequestDTO locationDTO,
            List<MultipartFile> imageList) {

        Location location = locationService.getLocation(locationDTO);
        List<Category> categoryList = categoryService.getCategoryList(itemDTO.getCategoryList());

        Item item = prepareNewItem(itemDTO, location, member);
        itemRepository.save(item);
        member.addItem(item);

        if (imageList != null) uploadItemImage(imageList, item);

        List<ItemCategory> itemCategoryList =
                itemCategoryService.createItemCategoryList(item, categoryList);

        categoryRepository.saveAll(categoryList);
        itemCategoryRepository.saveAll(itemCategoryList);
        itemElasticRepository.save(ItemConverter.toItemDocument(item, categoryList));
        return item.getId();
    }

    @Override
    public ItemPreviewListResponseDTO searchItem(ItemListRequestDTO requestDTO, int page) {

        String keyword = requestDTO.getKeyword();
        Category category = categoryService.findCategoryByName(requestDTO.getCategory().getName());
        String categoryName = category != null ? category.getName() : null;
        Location location = locationService.getLocation(requestDTO.getLocation());
        Pageable pageable = PageRequest.of(page, 4);

        Page<ItemDocument> itemDocumentPage =
                itemElasticRepository.searchItem(
                        keyword,
                        categoryName,
                        location.getCity(),
                        location.getDistrict(),
                        location.getNeighborhood(),
                        pageable);

        return ItemConverter.toItemPreviewResponseListDTO(itemDocumentPage);
    }

    private Item prepareNewItem(ItemRequestDTO requestDto, Location location, Member member) {
        Item item = ItemConverter.toItem(requestDto, location, member);
        item.setStatus(Item.Status.AVAILABLE);
        return item;
    }

    private void uploadItemImage(List<MultipartFile> imageList, Item item) {
        imageList.forEach(
                image -> {
                    Uuid uuid =
                            uuidRepository.save(Uuid.builder().uuid(UUID.randomUUID().toString()).build());
                    String keyName = amazonS3Manager.generateItemKeyName(uuid, item.getId());
                    String imageUri = amazonS3Manager.uploadFile(keyName, image);
                    item.addImage(ImageConverter.toItemImage(imageUri, item));
                });
    }
}
