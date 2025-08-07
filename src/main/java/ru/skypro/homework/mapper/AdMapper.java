package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
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
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);
    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "author.pk", target = "author")
    Ad toDto(AdEntity entity);

    @Mapping(target = "pk", ignore = true)
    @Mapping(source = "author", target = "author.pk")
    AdEntity toEntity(Ad dto);
}