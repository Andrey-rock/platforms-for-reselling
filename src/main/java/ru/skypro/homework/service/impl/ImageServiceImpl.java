package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${path.to.image.folder}")
    private String imageDir;

    private final ImageRepository imageRepository;


    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public boolean saveImage(MultipartFile image) throws IOException {
       ImageEntity im = new ImageEntity();

        Path path = Path.of(imageDir, im.getId() + "." + getExtension(image.getOriginalFilename()));
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);

        readAndWriteImage(image, path);

        ImageEntity imageEntity = imageRepository.findById(im.getId()).orElse(new ImageEntity());
        imageEntity.setFilePath(path.toString());
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
        imageEntity.setData(image.getBytes());

        imageRepository.save(imageEntity);
        return true;
    }

    @Override
    public ImageEntity getImage(Integer id) {
        imageRepository.findById(id).get();
        return null;
    }

    @Override
    public boolean updateImage(MultipartFile image, Integer id) {
        return false;
    }

    public void readAndWriteImage(MultipartFile file, Path path) throws IOException {
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
