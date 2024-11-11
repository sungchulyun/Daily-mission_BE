package dailymissionproject.demo.domain.image.service;

import dailymissionproject.demo.common.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class ImageService {

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

        String yearPath = "/" + calendar.get(Calendar.YEAR);
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
    public URL generatePostPresignedUrl(String fileName, String title){
        String name = title + "/" + fileName + getPostDir();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(name)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        return presigner.presignPutObject(putObjectPresignRequest).url();
    }
}
