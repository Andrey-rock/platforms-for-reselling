package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для объявлений.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ad {

    @Schema(description = "id автора объявления")
    private int author;
    @Schema(description = "ссылка на картинку объявления")
    private String image;
    @Schema(description = "id объявления")
    private int pk;
    @Schema(description = "цена объявления")
    private int price;
    @Schema(description = "заголовок объявления")
    private String title;
}
