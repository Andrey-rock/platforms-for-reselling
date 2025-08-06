package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Entity для комментарий.
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "Комментарии")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer pk;

    @Column(name = "время_создания")
    private Integer createdAt;

    @Column(name = "текст_комментария")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

}
