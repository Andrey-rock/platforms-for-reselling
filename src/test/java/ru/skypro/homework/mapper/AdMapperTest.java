package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

@SpringBootTest
public class AdMapperTest {

    @Autowired
    private AdMapper adMapper;

    // Тест маппинга AdEntity в Ad
    @Test
    void convertAdEntityToAd() {

        UserEntity author = new UserEntity();
        author.setId(10);
        AdEntity adEntity = new AdEntity(1, "descr", 100, "title", "image", author, List.of(new CommentEntity()));

        Ad ad = adMapper.toDto(adEntity);

        Assertions.assertNotNull(ad);
        Assertions.assertEquals(adEntity.getAuthor().getId(), ad.getAuthor());
        Assertions.assertEquals(adEntity.getTitle(), ad.getTitle());
        Assertions.assertEquals(adEntity.getPrice(), ad.getPrice());
        Assertions.assertEquals(adEntity.getImage(), ad.getImage());
        Assertions.assertEquals(adEntity.getPk(), ad.getPk());
    }

    // Тест маппинга Ad в AdEntity
    @Test
    void convertAdToAdEntity() {

        Ad ad = new Ad(1, "image", 1, 100, "title");

        AdEntity adEntity = adMapper.toEntity(ad);

        Assertions.assertNotNull(adEntity);
        Assertions.assertEquals(ad.getTitle(), adEntity.getTitle());
        Assertions.assertEquals(ad.getPrice(), adEntity.getPrice());
        Assertions.assertEquals(ad.getImage(), adEntity.getImage());
        Assertions.assertEquals(ad.getPk(), adEntity.getPk());
    }

    // Тест маппинга AdEntity в ExtendedAd
    @Test
    void convertAdEntityToExtendedAd() {

        UserEntity author = new UserEntity();
        author.setId(1);
        author.setUsername("JonDo");
        author.setFirstName("Jon");
        author.setLastName("Do");
        author.setPhone("89991111111");
        AdEntity adEntity = new AdEntity(1, "descr", 100, "title", "image", author, List.of(new CommentEntity()));

        ExtendedAd extendedAd = adMapper.toExtendedDto(adEntity);

        Assertions.assertNotNull(extendedAd);
        Assertions.assertEquals(adEntity.getTitle(), extendedAd.getTitle());
        Assertions.assertEquals(adEntity.getPrice(), extendedAd.getPrice());
        Assertions.assertEquals(adEntity.getImage(), extendedAd.getImage());
        Assertions.assertEquals(adEntity.getPk(), extendedAd.getPk());
        Assertions.assertEquals(adEntity.getAuthor().getPhone(), extendedAd.getPhone());
        Assertions.assertEquals(adEntity.getAuthor().getFirstName(), extendedAd.getAuthorFirstName());
        Assertions.assertEquals(adEntity.getAuthor().getLastName(), extendedAd.getAuthorLastName());
        Assertions.assertEquals(adEntity.getAuthor().getUsername(), extendedAd.getEmail());
    }

    // Тест маппинга CreateOrUpdateAd в AdEntity
    @Test
    void convertCreateOrUpdateAdToAdEntity() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd("title", 100, "descr");

        AdEntity adEntity = adMapper.toEntity(ad);

        Assertions.assertNotNull(adEntity);
        Assertions.assertEquals(ad.getTitle(), adEntity.getTitle());
        Assertions.assertEquals(ad.getPrice(), adEntity.getPrice());
        Assertions.assertEquals(ad.getDescription(), adEntity.getDescription());
    }
}
