package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;


public interface AdService  {

    Ads getAllAds();

    Ad addNewAds(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    ExtendedAd getInfoAboutAd(Integer id);

    void deleteAd(Integer id);

    CreateOrUpdateAd editInfoAboutAd(Integer id, CreateOrUpdateAd ad);

    Ads receiveAdsAuthorizeUser(String userName);

    boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException;
}
