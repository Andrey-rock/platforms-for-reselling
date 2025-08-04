package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для авторизации пользователя.
 *
 * @author skypro-backend
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Schema(description = "логин", minLength = 4,maxLength = 32)
    private String username;
    @Schema(description = "пароль", minLength = 8,maxLength = 16)
    private String password;
}
