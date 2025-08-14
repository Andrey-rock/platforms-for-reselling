package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.io.IOException;

public interface UserService {

    boolean setPassword(String username, NewPassword newPassword);

    User getUser(String username);

    UpdateUser updateUser(String username, UpdateUser updateUser);

    boolean updateUserImage(String username, MultipartFile file) throws IOException;
}
