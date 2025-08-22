package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с изображениями.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${path.to.images.folder}")
    private String imagesDir;

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Метод для загрузки изображения
     *
     * @param image - изображение в формате PNG, JPEG, GIF или TIFF.
     * @return Id изображения
     */
    @Override
    @Transactional
    public Integer uploadImage(String name, @NotNull MultipartFile image) throws IOException {

        log.info("Method for upload image start");

        Path filePath = Path.of(imagesDir, name + "." + getExtensions(Objects.requireNonNull(image.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        log.info("Avatar recording was successful");

        ImageEntity imageEntity = imageRepository.findByFilePath(filePath.toString());

        if (imageEntity != null) {
            imageRepository.delete(imageEntity);
        }
        imageEntity = new ImageEntity();
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
        imageEntity.setFilePath(filePath.toString());
        ImageEntity save = imageRepository.save(imageEntity);

        log.debug("Method for upload image end");

        return save.getId();
    }

    /**
     * Метод для получения изображения по его Id
     *
     * @param id - Id изображения
     * @return возвращает модель класса ImageEntity
     */
    @Override
    public ImageEntity getImage(Integer id) {

        return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }

    private @NotNull String getExtensions(@NotNull String fileName) {
        log.debug("Was invoked method for get extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}


