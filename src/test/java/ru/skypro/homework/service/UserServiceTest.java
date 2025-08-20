package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.*;

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
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserServiceImpl userService;

    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
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
    void testUpdateUserImageSuccess() throws IOException {

        MultipartFile file = mock(MultipartFile.class);

        UserEntity entity = new UserEntity();
        entity.setUsername(testUsername);

        when(userRepository.findByUsername(testUsername)).thenReturn(entity);

        when(imageService.uploadImage(file)).thenReturn(1);

        boolean result = userService.updateUserImage(testUsername, file);

        assertTrue(result);
        verify(imageService).uploadImage(file);
        verify(userRepository).save(entity);

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