package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для регистрации пользователя.
 *
 * @author skypro-backend
 * @version 0.0.1
 */
@Data
public class Register {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
