package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления пароля.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPassword {

    @Schema(description = "текущий пароль", minLength = 8,maxLength = 16)
    private String currentPassword;
    @Schema(description = "новый пароль", minLength = 8,maxLength = 16)
    private String newPassword;
}
