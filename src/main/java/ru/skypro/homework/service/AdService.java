package ru.skypro.homework.service;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;


public interface AdService  {

    Ads getAllAds();

    Ad addNewAds(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication);

    ExtendedAd getInfoAboutAd(Integer id);

    void deleteAd(Integer id);

    CreateOrUpdateAd editInfoAboutAd(Integer id, CreateOrUpdateAd ad);

    Ads receiveAdsAuthorizeUser();

    boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException;
}
