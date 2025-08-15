package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;

import java.io.IOException;

public interface ImageService {

    boolean saveImage(MultipartFile image) throws IOException;

    ImageEntity getImage(Integer id);

    boolean updateImage(MultipartFile image, Integer id);
}
