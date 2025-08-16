package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис управления пользователями.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */

@Service
public class UserServiceImpl implements UserService {

    // Папка для хранения аватарок пользователей на сервере
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserDetailsManager manager, PasswordEncoder encoder, UserMapper userMapper,
                           UserRepository userRepository) {
        this.manager = manager;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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
        UserDetails userDetails = manager.loadUserByUsername(username);
        String currentPass = newPassword.getCurrentPassword();
        String newPass = newPassword.getNewPassword();
        if (encoder.matches(currentPass, userDetails.getPassword())) {
            manager.changePassword(currentPass, encoder.encode(newPass));

            // Обновление пароля в таблице "пользователи". Будет удалено после нормализации БД
            // TODO: удалить после нормализации БД
            UserEntity userEntity = userRepository.findByUsername(username);
            userEntity.setPassword(encoder.encode(newPass));
            userRepository.save(userEntity);

            return true;
        }
        return false;
    }

    /**
     * Метод получения информации об авторизованном пользователе
     *
     * @return UpdateUser - DTO c информацией о пользователе
     */
    @Override
    public User getUser(String username) {
        return userMapper.toDto(userRepository.findByUsername(username));
    }

    /**
     * Метод обновления информации об авторизованном пользователе
     *
     * @param updateUser - DTO c информацией о пользователе
     *
     * @return UpdateUser - DTO c информацией о пользователе после обновления
     *
     * @throws RuntimeException, если пользователь не найден
     */
    @Override
    public UpdateUser updateUser(String username, UpdateUser updateUser) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userEntity.setFirstName(updateUser.getFirstName());
            userEntity.setLastName(updateUser.getLastName());
            userEntity.setPhone(updateUser.getPhone());
            userRepository.save(userEntity);
            return userMapper.updateUserFromEntity(userEntity);
        } else {
            // TODO: позже заменить на собственное исключение
            throw new RuntimeException("User not found");
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
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {

            Path filePath = Path.of(avatarsDir, username.substring(0, 4) + "."
                    + getExtensions(Objects.requireNonNull(file.getOriginalFilename())));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
            userEntity.setImage("/images/" + username.substring(0, 4) + "."
                    + getExtensions(Objects.requireNonNull(file.getOriginalFilename())));
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }

    private @NotNull String getExtensions(@NotNull String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
