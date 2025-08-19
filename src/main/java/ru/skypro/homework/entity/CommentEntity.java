package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Entity для комментарий.
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "комментарии")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_комментария")
    private Integer pk;

    @Column(name = "время_создания")
    private Long createdAt;

    @Column(name = "текст_комментария")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_пользователя")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_объявления")
    private AdEntity ad;
}
