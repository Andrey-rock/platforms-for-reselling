package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

/**
 * Контроллер для работы с пользователями
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Обновление пароля
     */
    @Operation(summary = "Обновление пароля")
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPasswordPayload) {
        boolean isCurrentPasswordValid = checkCurrentPassword(newPasswordPayload.getCurrentPassword());
        if (!isCurrentPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Некорректный текущий пароль");
        }
        boolean updateSuccess = updateUserPassword(newPasswordPayload.getNewPassword());
        if (updateSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Не удалось обновить пароль");
        }
    }

    private boolean checkCurrentPassword(String currentPassword) {
        return true;
    }

    private boolean updateUserPassword(String newPassword) {
        return true;
    }

    /**
     * Получение информации об авторизованном пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping("/me")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    /**
     * Обновление информации об авторизованном пользователе
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    /**
     * Обновление аватара авторизованного пользователя
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping("/me/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            return ResponseEntity.ok("Аватар успешно обновлён");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении аватара");
        }
    }
}