package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.service.ImageService;

@Tag(name = "Изображения")
@RestController
@RequestMapping("/images")
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Integer id) {
        ImageEntity image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] im = image.getData();
        return ResponseEntity.ok().body(im);
    }
}
