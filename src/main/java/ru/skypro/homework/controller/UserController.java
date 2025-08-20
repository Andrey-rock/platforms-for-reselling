package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

/**
 * Контроллер для работы с пользователями
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обновление пароля
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPasswordPayload, Authentication authentication) {

        logger.info("Controller method's for setting a new password");

        String username = authentication.getName();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не авторизованы");
        }
        boolean updateSuccess = userService.setPassword(authentication.getName(), newPasswordPayload);
        if (updateSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Старый пароль введен неверно");
        }
    }

    /**
     * Получение информации об авторизованном пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {

        logger.info("Controller method's for getting information about authorising user");

        String username = authentication.getName();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не авторизованы");
        }
        User user = userService.getUser(authentication.getName());
        return ResponseEntity.ok(user);
    }

    /**
     * Обновление информации об авторизованном пользователе
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser, Authentication authentication) {

        logger.info("Controller method's for updating information about authorising user");

        String username = authentication.getName();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не авторизованы");
        }
        UpdateUser update = userService.updateUser(authentication.getName(), updateUser);
        return ResponseEntity.ok(update);
    }

    /**
     * Обновление аватара авторизованного пользователя
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile imageFile, Authentication authentication) {

        logger.info("Controller method's for updating an authorising user's photo");

        try {
            if (userService.updateUserImage(authentication.getName(), imageFile)) {
                return ResponseEntity.ok("Аватар успешно обновлён");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не авторизованы");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении аватара");
        }
    }
}