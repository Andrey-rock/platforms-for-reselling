package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для добавления или обновления комментария.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class CreateOrUpdateComment {

    private String text;
}
