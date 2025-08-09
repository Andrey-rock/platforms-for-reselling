package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity для объявлений.
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "объявления")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_объявления")
    private Integer pk;

    @Column(name = "описание")
    private String description;

    @Column(name = "цена")
    private Integer price;

    @Column(name = "заголовок")
    private String title;

    @Column(name = "изображение")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;
}
