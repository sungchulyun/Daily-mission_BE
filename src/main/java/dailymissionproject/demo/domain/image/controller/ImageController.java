package dailymissionproject.demo.domain.image.controller;

import dailymissionproject.demo.domain.image.service.ImageService;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MissionRepository missionRepository;

    @GetMapping("/presigned-url/get")
    public ResponseEntity<?> getPresignedUrl(@RequestParam String fileName) {
        String presignedUrl = String.valueOf(imageService.generateGetPresignedUrl(fileName));
        return ResponseEntity.ok(presignedUrl);
    }

    @GetMapping("/presigned-url/put")
    public ResponseEntity<String> getPutPresignedUrl(@RequestParam String fileName, @RequestParam String title) {
        String presignedUrl = String.valueOf(imageService.generatePostPresignedUrl(fileName, title));
        return ResponseEntity.ok(presignedUrl);
    }

    @PostMapping("/save-image")
    public ResponseEntity<?> saveImage(@RequestParam String imageUrl){
        Mission mission = new Mission();
        mission.setImageUrl(imageUrl);

        missionRepository.save(mission);
        return ResponseEntity.ok("Mission Thumbnail saved Successfully");
    }
}
