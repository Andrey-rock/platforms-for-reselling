package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Контроллер для работы с изображениями
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Tag(name = "Изображения")
@RestController
@RequestMapping("/images")
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {

    Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Integer id) throws IOException {

        logger.info("Controller method's for getting image");

        ImageEntity image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Path.of(image.getFilePath());

        byte[] im = Files.readAllBytes(path);

        return ResponseEntity.ok().body(im);
    }
}
