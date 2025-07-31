package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для добавления или обновления объявления.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class CreateOrUpdateAd {

    private String title;
    private String description;
    private int price;
}
