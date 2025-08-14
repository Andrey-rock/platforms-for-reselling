package ru.skypro.homework.service.impl;

import jakarta.validation.constraints.NotNull;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AdServiceImpl implements AdService {

    Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    @Value("${path.to.image.folder}")
    private String avatarsDir;

    private final AdRepository adRepository;
    private final AdMapper adMapper;


    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
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
                        Authentication authentication) {
        logger.info("Method for Create new Ad");

        AdEntity adEntity = new AdEntity();

        adEntity.setTitle(properties.getTitle());
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());
        adEntity.setAuthor();
        adEntity.setImage();

        adRepository.save(adEntity);

        return adMapper.toEntity(adEntity);
    }

    @Override
    public ExtendedAd getInfoAboutAd(Integer id) {
        logger.info("Method for get Information about Ad");
        AdEntity adEntity = adRepository.findById(id).get();
        return adMapper.toEntity(adEntity);
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
        return adMapper.toEntity(adEntity);
    }

    @Override
    public Ads receiveAdsAuthorizeUser() {
        logger.info("Method for Receive ads authorize User");

        return adMapper.;
    }

    @Override
    public boolean renewImageAd(Integer id, MultipartFile imageFile) throws IOException {
        logger.info("Method for Renew image of Ad's");

        AdEntity adEntity = adRepository.findById(id).get();

        if (adEntity.getImage() != null) {
            Path filePath = Path.of(avatarsDir, getExtensions(Objects.requireNonNull(imageFile.getOriginalFilename())));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = imageFile.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
            adEntity.setImage(filePath.toFile().getAbsolutePath());
            adRepository.save(adEntity);
        }

        return true;
    }


    private @NotNull String getExtensions(@NotNull String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
