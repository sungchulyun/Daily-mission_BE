package dailymissionproject.demo.domain.image.controller;

import dailymissionproject.demo.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/presigned-url/put")
    public ResponseEntity<String> getPutPresignedUrl(@RequestParam String fileName, @RequestParam String title) {
        String presignedUrl = String.valueOf(imageService.generatePostPresignedUrl(fileName, title));
        return ResponseEntity.ok(presignedUrl);
    }
}
