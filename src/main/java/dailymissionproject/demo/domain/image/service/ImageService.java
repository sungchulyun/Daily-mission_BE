package dailymissionproject.demo.domain.image.service;

import dailymissionproject.demo.common.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
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

    public URL generateGetPresignedUrl(String fileName){

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(getObjectPresignRequest).url();
    }

    public URL generatePostPresignedUrl(String fileName){
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        return presigner.presignPutObject(putObjectPresignRequest).url();
    }


    /*
    public String uploadImg(MultipartFile file)throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = bucketUrl + "/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }
    }

     */
}
