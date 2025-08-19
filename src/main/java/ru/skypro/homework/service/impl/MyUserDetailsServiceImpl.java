package ru.skypro.homework.service.impl;

import org.jetbrains.annotations.NotNull;
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

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public MyUserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = new SecurityUser(userRepository.findByUsername(username));
        return new User(securityUser.getUsername(), securityUser.getPassword(),
                securityUser.getAuthorities());
    }

    public boolean userExists(String userName) {
        return userRepository.findByUsername(userName) != null;
    }

    public void createUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

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
