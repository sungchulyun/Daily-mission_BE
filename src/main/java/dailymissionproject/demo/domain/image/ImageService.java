package dailymissionproject.demo.domain.image;

import dailymissionproject.demo.common.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Util s3Util;


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
