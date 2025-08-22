package ru.skypro.homework.mapper;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "email")
    User toDto(UserEntity entity);

    @Mapping(source = "email", target = "username")
    UserEntity toEntity(User dto);

    default UserEntity UserEntityFromDto(@NotNull UserEntity entity, @NotNull UpdateUser updateUser){
        entity.setFirstName(updateUser.getFirstName());
        entity.setLastName(updateUser.getLastName());
        entity.setPhone(updateUser.getPhone());
        return entity;
    }

    UpdateUser updateUserFromEntity(UserEntity userEntity);

    UserEntity entityFromRegister(Register register);

}

