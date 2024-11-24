package dailymissionproject.demo.domain.image.service;

import dailymissionproject.demo.common.util.S3Util;
import dailymissionproject.demo.domain.image.dto.response.PresignedPostResponseDto;
import dailymissionproject.demo.domain.image.exception.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import static dailymissionproject.demo.domain.image.exception.ImageExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final static String FILE_SAVED_URL = "https://missionlabbucket.s3.ap-northeast-2.amazonaws.com";
    private final static int MAX_LENGTH = 100;

    private final S3Util s3Util;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * POST Thumbnail 디렉토리 생성
     * @return
     */
    public String getPostDir(){
        Calendar calendar = Calendar.getInstance();

        String yearPath = String.valueOf(calendar.get(Calendar.YEAR));
        String monthPath = yearPath + "/" + new DecimalFormat("00").format(calendar.get(Calendar.MONTH) + 1);
        String datePath = monthPath + "/" + new DecimalFormat("00").format(calendar.get(Calendar.DATE));

        return datePath;
    }

    /**
     * Post 썸네일 업로드
     */
    public String uploadPostS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName + getPostDir());
    }

    /**
     * User 썸네일 업로드
     */
    public String uploadUserS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName);
    }

    /**
     * Mission 썸네일 업로드
     */
    public String uploadMissionS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName);
    }

    /**
     * 파일 업로드를 위한 presignedUrl 생성 메서드
     * @param fileName
     * @param title
     * @return
     */
    public PresignedPostResponseDto generatePostPresignedUrl(String fileName, String title){
        //isRequestValid(fileName, title);

        String name = getPostDir() + "/" + title + "/" + UUID.randomUUID() + "_" + fileName;
        String filePath = FILE_SAVED_URL + "/" + name;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(name)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        String url = String.valueOf(presigner.presignPutObject(putObjectPresignRequest).url());

        return PresignedPostResponseDto.builder()
                .url(url)
                .path(filePath)
                .build();
    }


    private boolean isPatternValid(String input){
        if(Objects.isNull(input)) {
            throw new ImageException(INPUT_VALUE_IS_EMPTY);
        }

        if(!(input.matches("^[a-zA-Z0-9가-힣-_.\\s]+$"))){
            throw new ImageException(INPUT_VALUE_IS_NOT_VALID);
        }
        return true;
    }

    private boolean isLengthValid(String input){
        if(input.length() > MAX_LENGTH){
            throw new ImageException(INPUT_VALUE_LENGTH_IS_NOT_VALID);
        }
        return true;
    }

    public boolean isRequestValid(String filename, String title){
        isPatternValid(filename);
        isPatternValid(title);

        isLengthValid(filename);
        isLengthValid(title);

        return true;
    }
}
