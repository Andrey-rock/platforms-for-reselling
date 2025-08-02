package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для получения комментариев объявления.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int count;
    private Comment[] results;
}
