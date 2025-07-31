package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для обновления информации об авторизованном пользователе.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class UpdateUser {

    private String firstName;
    private String lastName;
    private String phone;
}
