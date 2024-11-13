package dailymissionproject.demo.domain.image.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "presigned-url 생성 요청 API", description = "업로드 권한이 부여된 presigned-url에 생성 요청할 때 사용하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "권한을 확인해주세요."),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/api/v1/image/presigned-url")
    public ResponseEntity<GlobalResponse> getPutPresignedUrl(@RequestParam String fileName, @RequestParam String title) {
        return ResponseEntity.ok(success(imageService.generatePostPresignedUrl(fileName, title)));
    }
}
