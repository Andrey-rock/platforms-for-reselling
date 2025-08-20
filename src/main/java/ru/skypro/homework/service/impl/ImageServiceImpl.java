package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.util.NoSuchElementException;

@Service
public class ImageServiceImpl implements ImageService {

    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Метод для загрузки изображения
     *
     * @param image - изображение в формате PNG, JPEG, GIF или TIFF.
     * @return  Id изображения
     */
    @Override
    @Transactional
    public Integer uploadImage(MultipartFile image) throws IOException {

        logger.info("Method for upload image");

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setData(image.getBytes());
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
        ImageEntity save = imageRepository.save(imageEntity);
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
        return imageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("картинка не найдена"));
    }
}


