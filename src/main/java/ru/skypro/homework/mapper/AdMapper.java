package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.dto.Ad;

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

    @Mapping(target = "author", ignore = true)
    AdEntity toEntity(Ad dto);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "author.username", target = "email")
    ExtendedAd toExtendedDto(AdEntity entity);

    @Mapping(source = "author.id", target = "author")
    @Mapping(target = "image", ignore = true)
    CreateOrUpdateAd toDtoAd(AdEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);
}