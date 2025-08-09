package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.skypro.homework.dto.Role;

import java.util.Collection;

/**
 * Entity для пользователей.
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "пользователи")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, name = "логин")
    private String username;

    @Column(name = "пароль")
    private String password;

    @Column(name = "имя")
    private String firstName;

    @Column(name = "фамилия")
    private String lastName;

    @Column(name = "телефон")
    private String phone;

    @Column(name = "роль")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "изображение")
    private String image;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties
    private Collection<AdEntity> ads;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties
    private Collection<CommentEntity> comments;

}
