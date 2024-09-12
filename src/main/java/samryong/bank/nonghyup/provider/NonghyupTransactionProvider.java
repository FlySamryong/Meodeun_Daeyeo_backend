package samryong.bank.nonghyup.provider;

import static org.springframework.http.HttpMethod.POST;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import samryong.bank.nonghyup.dto.request.BankTransferRequestDTO.DepositRequestDTO;
import samryong.bank.nonghyup.dto.request.BankTransferRequestDTO.WithdrawRequestDTO;
import samryong.bank.nonghyup.dto.response.CheckOpenFinAccountDirectResponseDTO;
import samryong.bank.nonghyup.dto.response.DrawingTransferResponseDTO;
import samryong.bank.nonghyup.dto.response.OpenFinAccountDirectResponseDTO;
import samryong.bank.nonghyup.dto.response.ReceivedTransferAccountNumberResponseDTO;
import samryong.bank.nonghyup.exception.NonghyupException;

@Component
@RequiredArgsConstructor
public class NonghyupTransactionProvider {

    @Value("${nonghyup.api.access-token}")
    private String accessToken; // 접근 토큰

    @Value("${nonghyup.api.iscd}")
    private String iscd; // 이용기관 코드

    @Value("${nonghyup.api.fintech-apsno}")
    private String fintechApsno; // 핀테크 앱 일련번호, 테스트 시 001로 고정

    @Value("${nonghyup.api.brdt-brno}")
    private String brdtBrno; // 생년월일/사업자번호

    // 핀테크 발급 요청, 계좌번호를 받아 핀테크 발급 확인을 위한 고유번호를 반환
    public String openFinAccountDirect(String accountNum) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("Header", createCommonHeader("OpenFinAccountDirect", "DrawingTransferA"));
        body.put("DrtrRgyn", "Y"); // 출금이체 등록여부
        body.put("BrdtBrno", brdtBrno); // 생년월일/사업자번호
        body.put("Bncd", "011"); // 농협계좌 은행코드
        body.put("Acno", accountNum); // 계좌번호

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<OpenFinAccountDirectResponseDTO> response =
                restTemplate.exchange(
                        "https://developers.nonghyup.com/OpenFinAccountDirect.nh",
                        POST,
                        request,
                        OpenFinAccountDirectResponseDTO.class);
        OpenFinAccountDirectResponseDTO responseBody = response.getBody();

        if (responseBody != null && responseBody.getHeader().getRpcd().equals("00000")) {
            return responseBody.getRgno();
        } else {
            throw new NonghyupException(
                    HttpStatus.BAD_REQUEST,
                    responseBody.getHeader().getRpcd(),
                    responseBody.getHeader().getRsms());
        }
    }

    // 핀테크 발급 확인
    public String checkOpenFinAccountDirect(String rgno) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("Header", createCommonHeader("CheckOpenFinAccountDirect", "DrawingTransferA"));
        body.put("Rgno", rgno); // 등록번호
        body.put("BrdtBrno", brdtBrno); // 생년월일/사업자번호

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<CheckOpenFinAccountDirectResponseDTO> response =
                restTemplate.exchange(
                        "https://developers.nonghyup.com/CheckOpenFinAccountDirect.nh",
                        POST,
                        request,
                        CheckOpenFinAccountDirectResponseDTO.class);
        CheckOpenFinAccountDirectResponseDTO responseBody = response.getBody();

        if (responseBody != null && responseBody.getHeader().getRpcd().equals("00000")) {
            return responseBody.getFinAcno();
        } else {
            throw new NonghyupException(
                    HttpStatus.BAD_REQUEST,
                    responseBody.getHeader().getRpcd(),
                    responseBody.getHeader().getRsms());
        }
    }

    // 입금 이체, 핀테크 기업 약정 계좌에서 개인 농협 계좌로 입금 이체
    public void receivedTransferAccountNumber(DepositRequestDTO requestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("Header", createCommonHeader("ReceivedTransferAccountNumber", "ReceivedTransferA"));
        body.put("Acno", requestDTO.getAccountNum()); // 계좌번호
        body.put("Tram", requestDTO.getTransferAmount()); // 거래금액
        body.put("DractOtlt", requestDTO.getWithdrawOtlt()); // 출금계좌인자내용
        body.put("MractOtlt", requestDTO.getDepositOtlt()); // 입금계좌인자내용
        body.put("Bncd", "011"); // 농협계좌 은행코드

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<ReceivedTransferAccountNumberResponseDTO> response =
                restTemplate.exchange(
                        "https://developers.nonghyup.com/ReceivedTransferAccountNumber.nh",
                        POST,
                        request,
                        ReceivedTransferAccountNumberResponseDTO.class);

        ReceivedTransferAccountNumberResponseDTO responseBody = response.getBody();

        if (responseBody != null && responseBody.getHeader().getRpcd().equals("00000")) {
            return;
        } else {
            throw new NonghyupException(
                    HttpStatus.BAD_REQUEST,
                    responseBody.getHeader().getRpcd(),
                    responseBody.getHeader().getRsms());
        }
    }

    // 출금 이체, 개인 농협 계좌에서 핀테크 기업 약정 계좌로 출금 이체
    public void drawingTransfer(WithdrawRequestDTO requestDTO) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("Header", createCommonHeader("DrawingTransfer", "DrawingTransferA"));
        body.put("FinAcno", requestDTO.getFinAcno()); // 핀-어카운트번호
        body.put("Tram", requestDTO.getTransferAmount()); // 거래금액
        body.put("DractOtlt", requestDTO.getWithdrawOtlt()); // 출금계좌인자내용

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<DrawingTransferResponseDTO> response =
                restTemplate.exchange(
                        "https://developers.nonghyup.com/DrawingTransfer.nh",
                        POST,
                        request,
                        DrawingTransferResponseDTO.class);

        DrawingTransferResponseDTO responseBody = response.getBody();

        if (responseBody != null && responseBody.getHeader().getRpcd().equals("00000")) {
            return;
        } else {
            throw new NonghyupException(
                    HttpStatus.BAD_REQUEST,
                    responseBody.getHeader().getRpcd(),
                    responseBody.getHeader().getRsms());
        }
    }

    // 공통 헤더부
    private Map<String, Object> createCommonHeader(String apiName, String apiSvcCd) {

        Map<String, Object> header = new HashMap<>();
        header.put("ApiNm", apiName); // API명
        header.put(
                "Tsymd", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))); // 전송일자
        header.put("Trtm", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"))); // 전송시간
        header.put("Iscd", iscd); // 이용기관코드
        header.put("FintechApsno", fintechApsno); // 핀테크 앱 일련번호
        header.put("ApiSvcCd", apiSvcCd); // API서비스코드
        header.put(
                "IsTuno",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))); // 기관거래고유번호
        header.put("AccessToken", accessToken); // 접근 토큰

        return header;
    }
}
