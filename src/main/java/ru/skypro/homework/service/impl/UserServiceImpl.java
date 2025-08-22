package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.*;


/**
 * Сервис управления пользователями.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final MyUserDetailsServiceImpl manager;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserServiceImpl(MyUserDetailsServiceImpl manager, UserMapper userMapper,
                           UserRepository userRepository, ImageService imageService) {
        this.manager = manager;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    /**
     * Метод установки нового пароля
     *
     * @param newPassword - DTO, содержащий старый и новый пароль
     * @return true, если старый пароль верный и обновление прошло успешно,
     * false, если старый пароль неверный
     */
    @Override
    public boolean setPassword(String username, @NotNull NewPassword newPassword) {

        log.info("Method for setting a new password set by user: {}", username);

        return manager.changePassword(username, newPassword);
    }

    /**
     * Метод получения информации об авторизованном пользователе
     *
     * @return UpdateUser - DTO c информацией о пользователе
     */
    @Override
    public User getUser(String username) {

        log.info("Method for get information about authorised user: {}", username);

        return userMapper.toDto(userRepository.findByUsername(username));
    }

    /**
     * Метод обновления информации об авторизованном пользователе
     *
     * @param updateUser - DTO c информацией о пользователе
     * @return UpdateUser - DTO c информацией о пользователе после обновления
     * @throws RuntimeException, если пользователь не найден
     */
    @Override
    public UpdateUser updateUser(String username, UpdateUser updateUser) {

        log.info("Method for update information about authorised user update: {}", username);

        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userEntity.setFirstName(updateUser.getFirstName());
            userEntity.setLastName(updateUser.getLastName());
            userEntity.setPhone(updateUser.getPhone());
            userRepository.save(userEntity);
            log.info("User {} updated", username);
            return userMapper.updateUserFromEntity(userEntity);
        } else {
            log.info("User {} not found", username);
            throw new UsernameNotFoundException("пользователь не найден");
        }
    }

    /**
     * Метод обновления аватара авторизованного пользователя
     *
     * @param file - изображение в формате PNG, JPEG, GIF или TIFF.
     * @return true, если пользователь авторизован и обновление прошло успешно,
     * false, если пользователь не авторизован
     */
    @Transactional
    @Override
    public boolean updateUserImage(String username, MultipartFile file) throws IOException {

        log.info("Method for update photo authorised user's: {}", username);

        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {

            userEntity.setImage("/images/" + imageService.uploadImage(file));
            userRepository.save(userEntity);
            log.info("User {} updated image success", username);
            return true;
        }
        log.info("User {} not found", username);
        return false;
    }
}
