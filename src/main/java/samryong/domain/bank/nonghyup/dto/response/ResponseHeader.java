package samryong.domain.bank.nonghyup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeader { // 응답 헤더

    @JsonProperty("Rpcd")
    private String Rpcd; // 응답코드, 정상처리코드 : 00000

    @JsonProperty("Rsms")
    private String Rsms; // 응답메시지
}
