package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.UserEntity;

/**
 * Mapper для объявлений.
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(source = "author.id", target = "author")
    Ad toDto(AdEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(source = "author", target = "author")
    AdEntity toEntity(Ad dto);

    default UserEntity map(int authorId) {
        if (authorId == 0) {
            return null;
        }
        return new UserEntity();
    }

    default int map(UserEntity user) {
        return user != null ? user.getId() : 0;
    }

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd toExtendedDto(AdEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);
}