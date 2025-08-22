package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.UserAlreadyExistException;
import ru.skypro.homework.exceptions.UserDoesNotExistException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;

import ru.skypro.homework.service.AuthService;

/**
 * Сервис аутентификации и регистрации.
 *
 * @author Andrei Bronskii
 * @author Lada Kozlova
 * @author AlanaSR, 2025
 * @version 0.0.1
 */

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsServiceImpl manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;


    public AuthServiceImpl(MyUserDetailsServiceImpl manager,
                           PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userMapper = userMapper;

    }

    /**
     * Метод для авторизации пользователя
     *
     * @param userName - логин пользователя
     * @param password - пароль пользователя
     * @return проверяет совпадение переданного пароля с сохраненным
     */
    @Override
    public boolean login(String userName, String password) {
        log.info("Method of the login user's start");
        if (!manager.userExists(userName)) {
            log.info("User {} does not exist", userName);
            throw new UserDoesNotExistException("Пользователь " + userName + " не зарегистрирован");
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            log.info("User {} does not match password", userName);
            throw new WrongPasswordException("Неверный пароль");
        }
        log.info("User {} successfully logged in", userName);
        return true;
    }

    /**
     * Метод для регистрации нового пользователя
     *
     * @param register - DTO для регистрации пользователя
     * @return boolean
     */
    @Override
    public boolean register(@NotNull Register register) {
        log.info("Method of the register user's start");
        if (manager.userExists(register.getUsername())) {
            log.info("User {} already exists", register.getUsername());
            throw new UserAlreadyExistException("Пользователь уже существует");
        }
        UserEntity userEntity = userMapper.entityFromRegister(register);
        userEntity.setPassword(encoder.encode(register.getPassword()));
        manager.createUser(userEntity);
        log.info("User {} successfully registered", register.getUsername());
        return true;
    }
}
