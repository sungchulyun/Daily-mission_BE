package dailymissionproject.demo.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")

    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String upload(MultipartFile file, String dirName)throws IOException {

        File uploadFile = convert(file).orElseThrow(() -> new IllegalStateException("MultipartFile -> File로 전환 실패했습니다."));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return "업로드 성공";
    }

    private Optional<File> convert(MultipartFile file)throws IOException {
        
        File convertFile = new File(genSerialNumber() + "_" + file.getOriginalFilename());
        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String genSerialNumber() {
        LocalDateTime now = LocalDateTime.now();
        String serial = "" + new DecimalFormat("0000").format(now.getYear())
                + new DecimalFormat("00").format(now.getMonthValue())
                + new DecimalFormat("00").format(now.getDayOfMonth())
                + new DecimalFormat("00").format(now.getHour())
                + new DecimalFormat("00").format(now.getMinute());

        return serial;
    }
}
