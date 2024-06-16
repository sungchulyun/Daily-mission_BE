package dailymissionproject.demo.domain.user.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dailymissionproject.demo.domain.user.dto.request.UpdateUserReqDto;
import dailymissionproject.demo.domain.user.dto.request.UserReqDto;
import dailymissionproject.demo.domain.user.dto.response.UserResDto;
import dailymissionproject.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketUrl;

    @PostMapping("/join")
    public UserResDto signUp(@RequestParam("file")MultipartFile file, @RequestBody UserReqDto userReqDto){
        return userService.join(userReqDto);
    }

    @PostMapping("/update/{name}")
    public void updateImg(@PathVariable("name") String name, @RequestParam("file")MultipartFile file)throws IOException {
        userService.updateProfile(name, file);
    }
}