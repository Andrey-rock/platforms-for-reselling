package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

/**
 * Контроллер для регистрации и авторизации
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

@RestController
public class RegistrationController {

    /**
     * Регистрация пользователя
     */
    @Operation(summary = "Регистрация пользователя")
    @Tag(name = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
    })
    @PostMapping("/register")
    public ResponseEntity<Register> registerNewUser (@RequestBody Register register) {
        return ResponseEntity.ok(register);
    }

    /**
     * Авторизация пользователя
     */
    @Operation(summary = "Авторизация пользователя")
    @Tag(name = "Авторизация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
    })
    @PostMapping("/login")
    public ResponseEntity<Login> loginUser(@RequestParam String username,
                                           @RequestParam String password) {
        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);
        return ResponseEntity.ok(login);
    }
}
