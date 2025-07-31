package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для комментариев.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Comment {

    private String author;
    private String authorImage;
    private String authorFirstName;
    private int createdAt;
    private int pk;
    private String text;
}
