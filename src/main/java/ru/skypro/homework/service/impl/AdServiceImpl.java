package ru.skypro.homework.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import org.springframework.security.core.Authentication;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.SecurityUtils;


import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объявлениями.
 *
 * @author AlanaSR, 2025
 * @version 0.0.1
 */

@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final SecurityUtils securityUtils;


    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository, ImageService imageService, SecurityUtils securityUtils) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.securityUtils = securityUtils;
    }

    /**
     * Метод получения всех объявлений
     *
     * @return список объявлений
     */
    @Override
    public Ads getAllAds() {
        log.info("Method for find All ads start");
        List<Ad> ads = adRepository.findAll().stream().
                map(adMapper::toDto).
                collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    /**
     * Метод для добавления новых объявлений
     *
     * @param properties - DTO модель класса CreateOrUpdate.
     * @param image      - изображение в формате PNG, JPEG, GIF или TIFF.
     * @return возвращает объявление в качестве DTO модели
     */
    @Override
    public Ad addNewAds(@NotNull CreateOrUpdateAd properties, MultipartFile image,
                        @NotNull Authentication authentication) throws IOException {
        log.info("Method for Create new Ad start");

        UserEntity user = userRepository.findByUsername(authentication.getName().describeConstable()
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName())));
        AdEntity adEntity = new AdEntity();

        adEntity.setTitle(properties.getTitle());
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());
        adEntity.setAuthor(user);
        adEntity.setImage("/images/" + imageService.uploadImage(user.getUsername(), image));
        adRepository.save(adEntity);

        log.info("Ad created");

        return adMapper.toDto(adEntity);
    }

    /**
     * Метод для получения информации по объявлению
     *
     * @param id - ID объявления
     * @return возвращает DTO объявления полученного по его Id
     */
    @Override
    public ExtendedAd getInfoAboutAd(Integer id) {
        log.info("Method for get Information about Ad: {}", id);
        AdEntity adEntity = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        return adMapper.toExtendedDto(adEntity);
    }

    /**
     * Метод удаления объявления
     *
     * @param id - ID объявления
     */
    @Override
    public void deleteAd(Integer id) {

        log.info("Method for Delete Ad with id: {}", id);

//Проверка прав на редактирование
        rightsVerification(id);

        adRepository.deleteById(id);
        log.info("Ad delete");
    }

    /**
     * Метод обновления информации объявления
     *
     * @param id - ID объявления
     * @param ad - DTO класса CreateOrUpdate
     * @return возвращает обновленное DTO объявления
     */
    @Override
    public CreateOrUpdateAd editInfoAboutAd(Integer id, @NotNull CreateOrUpdateAd ad) {

        log.info("Method for Edite Info about Ad with id: {}", id);

//Проверка прав на редактирование
        rightsVerification(id);

        AdEntity adEntity = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        adEntity.setTitle(ad.getTitle());
        adEntity.setPrice(ad.getPrice());
        adEntity.setDescription(ad.getDescription());

        adRepository.save(adEntity);
        log.info("Ad edit");
        return adMapper.toDtoAd(adEntity);
    }

    /**
     * Метод для получения объявлений авторизованного пользователя
     *
     * @param userName - логин пользователя
     * @return возвращать список объявлений пользователя
     */
    @Override
    public Ads receiveAdsAuthorizeUser(String userName) {
        log.info("Method for Receive ads authorize User: {}", userName);
        UserEntity user = userRepository.findByUsername(userName);
        List<Ad> ads = adRepository.findAllAdsByAuthor(user)
                .stream().map(adMapper::toDto)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    /**
     * Метод для обновления изображения объявления
     *
     * @param id        - ID объявления
     * @param imageFile - изображение в формате PNG, JPEG, GIF или TIFF.
     * @return boolean
     */
    @Override
    public boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException {
        log.info("Method for Renew image of Ad's: {}", id);

        AdEntity adEntity = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
        adEntity.setImage("/images/" + imageService.uploadImage(adEntity.getTitle() + adEntity.getPk(), imageFile));
        adRepository.save(adEntity);

        log.info("Ad renew");
        return true;
    }

    private void rightsVerification(int adId) {

        User currentUser = securityUtils.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {

            if (!(adRepository.getReferenceById(adId).getAuthor().getId()).equals(currentUser.getId())) {
                throw new AccessDeniedException("У вас нет прав на редактирование этого объявления");
            }
        }
    }
}
