package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для авторизации пользователя.
 *
 * @author skypro-backend
 * @version 0.0.1
 */
@Data
public class Login {

    private String username;
    private String password;
}
