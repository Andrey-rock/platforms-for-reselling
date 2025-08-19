package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import liquibase.exception.ChangeNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.util.NoSuchElementException;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${path.to.images.folder}")
    private String imagesDir;

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * @param image
     * @return
     * @throws IOException
     */
    @Override
    @Transactional
    public Integer uploadImage(MultipartFile image) throws IOException {

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setData(image.getBytes());
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
        ImageEntity save = imageRepository.save(imageEntity);
        return save.getId();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ImageEntity getImage(Integer id) {
        return imageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("картинка не найдена"));
    }
}


