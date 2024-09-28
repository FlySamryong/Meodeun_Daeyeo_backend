package samryong.domain.item.document;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;
import samryong.domain.location.document.LocationDocument;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "item", writeTypeHint = WriteTypeHint.FALSE)
@Setting(settingPath = "elastic/post-setting.json")
public class ItemDocument {

    @Id private Long id;

    @Field(type = Keyword, analyzer = "korean")
    private String name; // 상품명

    @Field(type = FieldType.Text, analyzer = "korean")
    private String description; // 상품 설명

    @Field(type = Keyword, analyzer = "korean")
    private String createdDate; // 상품 등록 날짜

    @Field(type = Keyword)
    private String status; // 상품 상태

    @Field(type = FieldType.Long)
    private Long period; // 대여 가능 기간

    @Field(type = FieldType.Long)
    private Long fee; // 대여료

    @Field(type = FieldType.Long)
    private Long deposit; // 보증금

    @Field(type = Keyword)
    private List<String> imageUrlList; // 이미지 URL 목록

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<CategoryDocument> itemCategoryList; // 상품 카테고리 목록

    @Field(type = FieldType.Nested, includeInParent = true)
    private LocationDocument location; // 위치 정보
}
