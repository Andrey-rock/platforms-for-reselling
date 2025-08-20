package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity для изображений.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Data
@Entity
@Table(name = "изображения")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "размер_файла")
    private long fileSize;
    @Column(name = "тип_файла")
    private String mediaType;

    @Column(name = "содержимое_файла")
    private byte[] data;
}
