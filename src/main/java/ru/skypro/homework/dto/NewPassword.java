package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для обновления пароля.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class NewPassword {

    private String currentPassword;
    private String newPassword;
}
