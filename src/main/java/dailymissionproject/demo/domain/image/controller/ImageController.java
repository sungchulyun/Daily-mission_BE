package dailymissionproject.demo.domain.image.controller;

import dailymissionproject.demo.common.config.response.GlobalResponse;
import dailymissionproject.demo.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dailymissionproject.demo.common.config.response.GlobalResponse.success;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/api/v1/image/presigned-url")
    public ResponseEntity<GlobalResponse> getPutPresignedUrl(@RequestParam String fileName, @RequestParam String title) {
        return ResponseEntity.ok(success(imageService.generatePostPresignedUrl(fileName, title)));
    }
}
