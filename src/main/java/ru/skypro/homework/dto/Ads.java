package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для получения объявлений.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;
    private Ad[] results;
}
