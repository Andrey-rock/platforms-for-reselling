package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.dto.Comment;
/**
 * Mapper для комментариев.
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.image", target = "authorImage")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "pk", target = "pk")
    Comment toDto(CommentEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "authorImage", target = "author.image")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    CommentEntity toEntity(Comment dto);
}