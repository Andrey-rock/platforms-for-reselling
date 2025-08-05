package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import ru.skypro.homework.dto.User;

/**
 * Entity для объявлений.
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "Объявления")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
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
