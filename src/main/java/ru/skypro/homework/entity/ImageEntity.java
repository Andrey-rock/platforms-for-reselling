package ru.skypro.homework.entity;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class ImageEntity {
    private Integer id;
    private String filePath;
    private long fileSize;
    private String mediaType;

    @Lob
    private byte[] data;
}
