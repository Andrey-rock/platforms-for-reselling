package ru.skypro.homework.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.UserAlreadyExistException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;

import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


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
     *Метод для авторизации пользователя
     *
     * @param userName - логин пользователя
     * @param password - пароль пользователя
     * @return проверяет совпадение переданного пароля с сохраненным
     */
    @Override
    public boolean login(String userName, String password) {
        logger.info("Method of the login user's");
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())){
            throw new WrongPasswordException("Неверный пароль");
        }
        return true;
    }

    /**
     *Метод для регистрации нового пользователя
     * @param register - DTO для регистрации пользователя
     * @return boolean
     */
    @Override
    public boolean register(@NotNull Register register) {
        logger.info("Method of the register user's");
        if (manager.userExists(register.getUsername())) {
            throw new UserAlreadyExistException("Пользователь уже существует");
        }
        UserEntity userEntity = userMapper.entityFromRegister(register);
        userEntity.setPassword(encoder.encode(register.getPassword()));
        manager.createUser(userEntity);

        return true;
    }
}
