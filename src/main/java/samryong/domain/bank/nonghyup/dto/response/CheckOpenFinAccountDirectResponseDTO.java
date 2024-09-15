package samryong.domain.bank.nonghyup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckOpenFinAccountDirectResponseDTO { // 핀테크 발급 확인 API 응답 DTO

    @JsonProperty("FinAcno")
    private String FinAcno; // 핀-어카운트번호

    @JsonProperty("Header")
    private ResponseHeader Header; // 응답헤더
}
