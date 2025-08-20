package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование UserService
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDetailsManager manager;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private String testUsername = "testuser";
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
    }
// Тестирование setPassword. Успешная смена пароля
    @Test
    public void testSetPasswordSuccess() {
        when(encoder.encode(anyString())).thenAnswer(invocation -> {
            String raw = invocation.getArgument(0);
            return "hashed_" + raw;
        });

        when(encoder.matches("currentPass", "hashed_current")).thenReturn(true);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("hashed_current");

        when(userRepository.findByUsername("testuser")).thenReturn(userEntity);

        when(manager.loadUserByUsername(anyString())).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        "testuser",
                        "hashed_current",
                        true, true, true, true,
                        new ArrayList<>()
                )
        );

        doNothing().when(manager).changePassword(anyString(), anyString());

        NewPassword newPassword = new NewPassword("currentPass", "newPass");

        boolean result = userService.setPassword("testuser", newPassword);

        System.out.println("Result of password update: " + result);
        System.out.println("Password in the database entity: " + userEntity.getPassword());

        assertTrue(result, "Password should have been updated successfully.");

        verify(manager).changePassword(eq("currentPass"), eq("hashed_newPass"));

        verify(userRepository).save(argThat(user -> user.getPassword().equals("hashed_newPass")));
    }

    // Тестирование setPassword. Старый пароль был введен неверно
    @Test
    public void testSetPasswordWrongOldPassword() {
        String username = "testuser";
        userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("hashed_current");

        when(manager.loadUserByUsername("testuser")).thenReturn(userDetails);

        lenient().when(encoder.matches(anyString(), anyString())).thenReturn(false);

        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongOldPass");
        newPassword.setNewPassword("newPass123");

        boolean result = userService.setPassword(username, newPassword);

        assertFalse(result);

        verify(manager, never()).changePassword(anyString(), anyString());

        verify(manager).loadUserByUsername(username);

        verify(encoder).matches("wrongOldPass", userDetails.getPassword());
    }

    // Тестирование getUser
    @Test
    void testGetUserReturnsMappedUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername(testUsername);

        ru.skypro.homework.dto.User dto = new ru.skypro.homework.dto.User();

        when(userRepository.findByUsername(testUsername)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        User result = userService.getUser(testUsername);

        assertEquals(dto, result);
        verify(userRepository).findByUsername(testUsername);
    }

    // Тестирование updateUser. Успешное обновление данных
    @Test
    void testUpdateUserSuccess() {

        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("John");
        updateUser.setLastName("Doe");
        updateUser.setPhone("1234567890");

        UserEntity entity = new UserEntity();

        when(userRepository.findByUsername(testUsername)).thenReturn(entity);

        when(userMapper.updateUserFromEntity(entity)).thenReturn(new UpdateUser());

        UpdateUser result = userService.updateUser(testUsername, updateUser);

        assertNotNull(result);
        verify(userRepository).save(entity);
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("1234567890", entity.getPhone());
    }

    // Тестирование updateUser. Пользователь не найден
    @Test
    void testUpdateUserUserNotFound() {

        when(userRepository.findByUsername(testUsername)).thenReturn(null);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> userService.updateUser(testUsername, new UpdateUser()));
        assertEquals("User not found", thrown.getMessage());
    }

    // Тестирование updateUserImage. Успешное обновление аватара
    @Test
    void testUpdateUserImageSuccess() throws IOException, NoSuchFieldException, IllegalAccessException {

        MultipartFile file = mock(MultipartFile.class);
        String filename = "avatar.png";

        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        UserEntity entity = new UserEntity();

        when(userRepository.findByUsername(testUsername)).thenReturn(entity);

        Path tempDir = Files.createTempDirectory("avatars");
        String avatarsDirPath = tempDir.toString();

//        Field field = UserServiceImpl.class.getDeclaredField("avatarsDir");
//        field.setAccessible(true);
//        field.set(userService, avatarsDirPath);

        boolean result = userService.updateUserImage(testUsername, file);

        assertTrue(result);

    }
    // Тестирование updateUserImage. Пользователь не найден
    @Test
    void testUpdateUserImageUserNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        boolean result = userService.updateUserImage("nonexistent", file);

        assertFalse(result);
    }
}