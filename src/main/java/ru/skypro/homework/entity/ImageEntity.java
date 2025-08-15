package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "изображения")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filePath;
    private long fileSize;
    private String mediaType;

    @Lob
    private byte[] data;
}
