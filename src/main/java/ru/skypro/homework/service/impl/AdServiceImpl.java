package ru.skypro.homework.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import org.springframework.security.core.Authentication;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AdServiceImpl implements AdService {

    Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;


    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public Ads getAllAds() {
        logger.info("Method for find All ads");
        List<Ad> ads = adRepository.findAll().stream().
                map(adMapper::toDto).
                collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    @Override
    public Ad addNewAds(CreateOrUpdateAd properties, MultipartFile image,
                        Authentication authentication) throws IOException {
        logger.info("Method for Create new Ad");

        UserEntity user = userRepository.findByUsername(authentication.getName());
        AdEntity adEntity = new AdEntity();

        adEntity.setTitle(properties.getTitle());
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());
        adEntity.setAuthor(user);
        adEntity.setImage("/images/" + imageService.uploadImage(image));
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
        UserEntity user = userRepository.findByUsername(userName);
        List<Ad> ads = adRepository.findAllAdsByAuthor(user)
                .stream().map(adMapper::toDto)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    @Override
    public boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException {
        logger.info("Method for Renew image of Ad's");

        AdEntity adEntity = adRepository.findById(id).get();

        adEntity.setImage("/images/" + imageService.uploadImage(imageFile));

        adRepository.save(adEntity);

        return true;
    }
}
