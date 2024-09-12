package samryong.bank.nonghyup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFinAccountDirectResponseDTO { // 핀테크 핀-어카운트 직접발급 API 응답 DTO

    @JsonProperty("Rgno")
    private String Rgno; // 등록번호

    @JsonProperty("Header")
    private ResponseHeader Header; // 응답헤더
}
