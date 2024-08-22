package dailymissionproject.demo.common.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Util {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketUrl;

    /**
     * 설명 : 현재 날짜, 시간에 해당하는  serial Number 생성
     *      이미지 저장 시 중복되는 이름이 있을 경우 ovveride를 방지하기 위해
     *      이미지 이름 앞에 붙여 저장
     *      ex) 202408232211
     * @return String
     */
    public String genSerialNumber(){
        LocalDateTime now = LocalDateTime.now();
        String serial = "" + new DecimalFormat("0000").format(now.getYear())
                + new DecimalFormat("00").format(now.getMonthValue())
                + new DecimalFormat("00").format(now.getDayOfMonth())
                + new DecimalFormat("00").format(now.getHour())
                + new DecimalFormat("00").format(now.getMinute());

        return serial;
    }

    public String upload(MultipartFile file, String dirName) throws IOException {

        File uploadFile = convert(file).orElseThrow(() -> new IllegalStateException("MultipartFile -> File로 전환 실패했습니다."));

        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName){

        amazonS3Client.putObject(bucket, bucketUrl + fileName, uploadFile);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3 업로드 전 로컬에 생성된 File 삭제
     * @param file
     */
    private void removeNewFile(File file){

        if(file.delete()){
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    /**
     * 설명 : Request 받은 MultipartFile -> File 전환
     * @param file
     * @return File
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(genSerialNumber() + "_" + file.getOriginalFilename());
        if(convertFile.createNewFile()){
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
