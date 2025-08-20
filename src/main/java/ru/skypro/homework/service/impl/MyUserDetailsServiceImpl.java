package ru.skypro.homework.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.entity.SecurityUser;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(MyUserDetailsServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public MyUserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /**
     * Метод для получения данных пользователя по его логину
     *
     * @param username - логин пользователя
     * @return данные пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Method for loading user's by username");

        SecurityUser securityUser = new SecurityUser(userRepository.findByUsername(username));
        return new User(securityUser.getUsername(), securityUser.getPassword(),
                securityUser.getAuthorities());
    }

    /**
     *Метод для проверки существования польователя
     *
     * @param userName - логин пользователя
     * @return boolean
     */
    public boolean userExists(String userName) {

        logger.info("Method for checking existing user");

        return userRepository.findByUsername(userName) != null;
    }

    /**
     * Метод для создания нового пользвоателя
     *
     * @param userEntity - Entity для пользователей.
     */
    public void createUser(UserEntity userEntity) {

        logger.info("Method for create new user");

        userRepository.save(userEntity);
    }

    /**
     * Метод для обновления пароля пользователя
     *
     * @param username - логин пользователя
     * @param newPassword - новый пароль
     * @return boolean
     */
    public boolean changePassword(String username, @NotNull NewPassword newPassword) {
        UserEntity userEntity = userRepository.findByUsername(username);
        String currentPass = newPassword.getCurrentPassword();
        String newPass = newPassword.getNewPassword();
        if (encoder.matches(currentPass, userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(newPass));
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}
