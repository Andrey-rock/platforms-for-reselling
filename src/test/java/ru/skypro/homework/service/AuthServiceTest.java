package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.SecurityUser;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.MyUserDetailsServiceImpl;

/**
 * Тестирование AuthService
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
public class AuthServiceTest {

    private MyUserDetailsServiceImpl manager;
    private PasswordEncoder encoder;
    private UserMapperImpl userMapperImpl;
    private UserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        manager = mock(MyUserDetailsServiceImpl.class);
        encoder = mock(PasswordEncoder.class);
        userMapperImpl = mock(UserMapperImpl.class);
        userRepository = mock(UserRepository.class);

        authService = new AuthServiceImpl(manager, encoder, userMapperImpl);
    }

    // Тестирование метода login. Успешный вход
    @Test
    void testLoginSuccess() {
        String username = "user1";
        String password = "qwerty";

        UserDetails userDetails = mock(UserDetails.class);
        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("hashedPassword");
        when(encoder.matches(password, "hashedPassword")).thenReturn(true);

        boolean result = authService.login(username, password);

        assertTrue(result);
        verify(manager).userExists(username);
        verify(manager).loadUserByUsername(username);
        verify(encoder).matches(password, "hashedPassword");
    }

    // Тестирование метода login. Несуществующий пользователь
    @Test
    void testLoginUserDoesNotExist() {
        String username = "user2";
        String password = "123456";

        when(manager.userExists(username)).thenReturn(false);

        boolean result = authService.login(username, password);

        assertFalse(result);
        verify(manager).userExists(username);
        verifyNoMoreInteractions(manager);
    }

    // Тестирование метода login. Неправильный пароль
    @Test
    void testLoginWrongPassword() {
        String username = "user3";
        String password = "987654";

        UserDetails userDetails = mock(UserDetails.class);
        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("hashedPassword");
        when(encoder.matches(password, "hashedPassword")).thenReturn(false);

        boolean result = authService.login(username, password);

        assertFalse(result);
    }

    // Тестирование метода register. Успешная регистрация
    @Test
    void testRegisterSuccess() {
        Register register = new Register();
        register.setUsername("newUser");
        register.setPassword("newPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("newUser");
        userEntity.setPassword("somePassword");

        when(manager.userExists(register.getUsername())).thenReturn(false);
        when(userMapperImpl.entityFromRegister(register)).thenReturn(userEntity);

        when(encoder.encode(register.getPassword())).thenReturn("encodedPass");

        boolean result = authService.register(register);

        assertTrue(result);

        verify(encoder).encode(register.getPassword());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(manager).createUser(userCaptor.capture());

        UserEntity createdUser = userCaptor.getValue();

        assertEquals("newUser", createdUser.getUsername());

        verify(userRepository).save(userEntity);

        assertEquals("encodedPass", userEntity.getPassword());
    }

    // Тестирование метода register. Попытка зарегистрировать уже существующего пользователя
    @Test
    void testRegisterUserAlreadyExists() {
        Register register = new Register();
        register.setUsername("existingUser");

        when(manager.userExists(register.getUsername())).thenReturn(true);

        boolean result = authService.register(register);

        assertFalse(result);

        verify(manager).userExists(register.getUsername());
        verifyNoMoreInteractions(userMapperImpl, encoder, manager, userRepository);
    }
}