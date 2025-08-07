package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.User;
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
}