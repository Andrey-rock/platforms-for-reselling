package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;

/**
 * Mapper для пользователей.
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "email")
    User toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "email", target = "username")
    UserEntity toEntity(User dto);

    default void updateEntityFromDto(UpdateUser updateUser, @org.jetbrains.annotations.NotNull UserEntity entity) {
        if (updateUser == null || entity == null) {
            return;
        }
        if (updateUser.getFirstName() != null) {
            entity.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null) {
            entity.setLastName(updateUser.getLastName());
        }
        if (updateUser.getPhone() != null) {
            entity.setPhone(updateUser.getPhone());
        }
    }

    default UserEntity loginToEntity(Login login) {
        if (login == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(login.getUsername());
        return entity;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", constant = "")
    UserEntity fromRegister(Register register);

    default void updatePassword(UserEntity user, NewPassword newPassword) {
        if (newPassword != null && user != null) {
            user.setPassword(newPassword.getNewPassword());
        }
    }
}