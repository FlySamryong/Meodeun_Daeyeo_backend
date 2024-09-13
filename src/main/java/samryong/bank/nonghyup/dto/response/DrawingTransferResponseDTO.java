package samryong.bank.nonghyup.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawingTransferResponseDTO { // 출금 이체 API 응답 DTO

    @JsonProperty("Header")
    private ResponseHeader Header; // 응답헤더

    @JsonProperty("FinAcno")
    private String FinAcno; // 핀-어카운트번호
}
