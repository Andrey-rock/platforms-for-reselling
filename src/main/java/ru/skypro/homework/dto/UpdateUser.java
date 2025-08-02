package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления информации об авторизованном пользователе.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class UpdateUser {

    @Schema(description = "имя пользователя", minLength = 3,maxLength = 10)
    private String firstName;
    @Schema(description = "фамилия пользователя", minLength = 3,maxLength = 10)
    private String lastName;
    @Schema(description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
