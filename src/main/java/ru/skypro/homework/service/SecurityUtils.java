package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.User;

/**
 * Сервис для предоставления имени текущего авторизованного пользователя
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Slf4j
@Service
public class SecurityUtils {

    private final UserService userService; // или другой сервис, который возвращает DTO по username

    public SecurityUtils(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser() {
        log.debug("getCurrentUser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("User not logged in");
            throw new RuntimeException("Пользователь не авторизован");
        }
        String username = authentication.getName(); // обычно это username
        log.debug("Username : {}", username);
        return userService.getUser(username);
    }
}