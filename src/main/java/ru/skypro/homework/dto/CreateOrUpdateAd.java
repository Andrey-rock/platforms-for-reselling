package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для добавления или обновления объявления.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления", minLength = 4, maxLength = 32)
    private String title;
    @Schema(description = "цена объявления", minLength = 0, maxLength = 10000000)
    private int price;
    @Schema(description = "описание объявления", minLength = 8, maxLength = 64)
    private String  description;
}
