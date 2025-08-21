package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;

/**
 * Сервис для работы с изображениями.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

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
    public Integer uploadImage(@NotNull MultipartFile image) throws IOException {

        log.info("Method for upload image start");

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setData(image.getBytes());
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
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
}


