package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для добавления или обновления объявления.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления", minLength = 4, maxLength = 32)
    private String title;
    @Schema(description = "цена объявления", minLength = 0, maxLength = 10000000)
    private String description;
    @Schema(description = "описание объявления", minLength = 8, maxLength = 64)
    private int price;
}
