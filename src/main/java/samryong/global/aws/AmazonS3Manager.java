package samryong.global.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import samryong.domain.uuid.Uuid;
import samryong.domain.uuid.UuidRepository;
import samryong.global.code.GlobalErrorCode;
import samryong.global.config.AmazonS3Config;
import samryong.global.exception.GlobalException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final AmazonS3Config amazonConfig;
    private final UuidRepository uuidRepository;

    private static final Map<String, String> CONTENT_TYPE_MAP =
            Map.of(
                    "png", MediaType.IMAGE_PNG_VALUE,
                    "jpg", MediaType.IMAGE_JPEG_VALUE,
                    "jpeg", MediaType.IMAGE_JPEG_VALUE,
                    "gif", MediaType.IMAGE_GIF_VALUE);

    public String uploadFile(String keyName, MultipartFile file) {

        validateFile(file);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(determineContentType(file.getOriginalFilename()));

        try {
            amazonS3.putObject(
                    new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("Error at AmazonS3Manager uploadFile: {}", e.getMessage(), e);
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
        }
    }

    private String determineContentType(String originalFilename) {
        if (originalFilename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        String extension =
                originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        return CONTENT_TYPE_MAP.getOrDefault(extension, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public String generateItemKeyName(Uuid uuid, Long itemId) {
        return amazonConfig.getItemPath() + '/' + itemId + '/' + uuid.getUuid();
    }

    public String generateMemberKeyName(Uuid uuid, Long memberId) {
        return amazonConfig.getMemberPath() + '/' + memberId + '/' + uuid.getUuid();
    }
}
