package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    // Тест маппинга UserEntity в User
    @Test
    public void convertUserEntityToUser() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("JonDo");
        userEntity.setFirstName("JonD");
        userEntity.setLastName("Do");
        userEntity.setPhone("89991111111");
        userEntity.setRole(Role.USER);
        userEntity.setImage("image");

        User user = userMapper.toDto(userEntity);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userEntity.getFirstName(), user.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), user.getLastName());
        Assertions.assertEquals(userEntity.getUsername(), user.getEmail());
        Assertions.assertEquals(userEntity.getPhone(), user.getPhone());
        Assertions.assertEquals(userEntity.getRole(), user.getRole());
        Assertions.assertEquals(userEntity.getImage(), user.getImage());
    }

    // Тест маппинга User в UserEntity
    @Test
    public void convertUserToUserEntity() {

        User user = new User();
        user.setId(1);
        user.setEmail("JonDo");
        user.setFirstName("JonD");
        user.setLastName("Do");
        user.setPhone("89991111111");
        user.setRole(Role.USER);
        user.setImage("image");

        UserEntity userEntity = userMapper.toEntity(user);

        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(user.getFirstName(), userEntity.getFirstName());
        Assertions.assertEquals(user.getLastName(), userEntity.getLastName());
        Assertions.assertEquals(user.getEmail(), userEntity.getUsername());
        Assertions.assertEquals(user.getPhone(), userEntity.getPhone());
        Assertions.assertEquals(user.getRole(), userEntity.getRole());
        Assertions.assertEquals(user.getImage(), userEntity.getImage());
    }

    // Тест маппинга UpdateUser в UserEntity
    @Test
    public void convertUpdateUserToUserEntity() {

        UpdateUser user = new UpdateUser();
        user.setFirstName("JonDo");
        user.setLastName("Do");
        user.setPhone("89991111111");


        UserEntity userEntity = userMapper.UserEntityFromDto(user);

        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(user.getFirstName(), userEntity.getFirstName());
        Assertions.assertEquals(user.getLastName(), userEntity.getLastName());
        Assertions.assertEquals(user.getPhone(), userEntity.getPhone());
    }
}
