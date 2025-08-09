package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entity.UserEntity;

/**
 * Mapper для комментариев.
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);


    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "pk", target = "pk")
    Comment toDto(CommentEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(source = "authorImage", target = "author.image")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    CommentEntity toEntity(Comment dto);

    default UserEntity map(int authorId) {
        if (authorId == 0) {
            return null;
        }
        return new UserEntity();
    }

    default int map(UserEntity user) {
        return user != null ? user.getId() : 0;
    }
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment comment);
}