package samryong.domain.location.document;

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

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "location", writeTypeHint = WriteTypeHint.FALSE)
@Setting(settingPath = "elastic/post-setting.json")
public class LocationDocument {

    @Id private Long id;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String city;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String district;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String neighborhood;
}
