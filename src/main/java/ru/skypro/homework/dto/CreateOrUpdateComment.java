package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для добавления или обновления комментария.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    private String text;
}
