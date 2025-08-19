package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;

import java.io.IOException;

public interface ImageService {

    Integer uploadImage(MultipartFile image) throws IOException;

    ImageEntity getImage(Integer id);
}
