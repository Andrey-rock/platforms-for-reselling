package ru.skypro.homework.service.impl;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AdServiceImpl implements AdService {

    Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    @Value("${path.to.image.folder}")
    private String imageDir;

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserServiceImpl userService;
    private final ImageEntity imageEntity;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageService;


    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, UserServiceImpl userService, ImageEntity imageEntity, UserRepository userRepository, ImageServiceImpl imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userService = userService;
        this.imageEntity = imageEntity;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public Ads getAllAds() {
        logger.info("Method for find All ads");
        List<Ad> ads = adRepository.findAll().stream().
                map(dto -> adMapper.toDto(dto)).
                collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    @Override
    public Ad addNewAds(CreateOrUpdateAd properties, MultipartFile image,
                        Authentication authentication) throws IOException {
        logger.info("Method for Create new Ad");

        UserEntity user = userService.getUserEntity(authentication);
        AdEntity adEntity = new AdEntity();

        adEntity.setTitle(properties.getTitle());
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());
        adEntity.setAuthor(user);

        Path filePath = Path.of(imageDir, adEntity.hashCode() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename()));
        adEntity.setImage(filePath.toString());

        adRepository.save(adEntity);

        return adMapper.toDto(adEntity);
    }

    @Override
    public ExtendedAd getInfoAboutAd(Integer id) {
        logger.info("Method for get Information about Ad");
        AdEntity adEntity = adRepository.findById(id).get();
        return adMapper.toExtendedDto(adEntity);
    }

    @Override
    public void deleteAd(Integer id) {
        logger.info("Method for Deleting Ad");
        adRepository.deleteById(id);
    }

    @Override
    public CreateOrUpdateAd editInfoAboutAd(Integer id, CreateOrUpdateAd ad) {
        logger.info("Method for Edite Info about Ad");
        AdEntity adEntity = adRepository.findById(id).get();
        adEntity.setTitle(ad.getTitle());
        adEntity.setPrice(ad.getPrice());
        adEntity.setDescription(ad.getDescription());

        adRepository.save(adEntity);
        return adMapper.toDtoAd(adEntity);
    }

    @Override
    public Ads receiveAdsAuthorizeUser(String userName) {
        logger.info("Method for Receive ads authorize User");
        UserEntity author = userRepository.findUserEntityByUserName(userName);
        List<Ad> ads = adRepository.findAllAdsByAuthor(userName)
                .stream().map(ad -> adMapper.toDto(ad))
                .collect(Collectors.toList());
        Ads adsDto = new Ads(ads.size(), ads);
        return adsDto;
    }

    @Override
    public boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException {
        logger.info("Method for Renew image of Ad's");

        AdEntity adEntity = adRepository.findById(id).get();

        Path filePath = Path.of(imageDir, adEntity.hashCode() + "." + StringUtils.getFilenameExtension(imageFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        imageService.readAndWriteImage(imageFile, filePath);

        adEntity.setImage(filePath.toString());
        adRepository.save(adEntity);

        return true;
    }


    private @NotNull String getExtensions(@NotNull String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
