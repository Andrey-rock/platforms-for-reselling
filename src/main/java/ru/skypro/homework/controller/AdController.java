package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;

/**
 * Контроллер для работы с объявлениями
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Объявления")
@RestController
@RequestMapping("/ads")
public class AdController {


    private final AdServiceImpl adService;

    public AdController(AdServiceImpl adService) {
        this.adService = adService;
    }

    /**
     * Получение всех объявлений
     */
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public Ads getAllAds() {
        return adService.getAllAds();
    }

    /**
     * Добавление объявления
     */
    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addNewAds(@RequestPart("properties") CreateOrUpdateAd properties,
                        @RequestPart("image") MultipartFile image,
                        Authentication authentication) throws IOException {
        return adService.addNewAds(properties, image, authentication);
    }

    /**
     * Получение информации об объявлении
     */
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @GetMapping("{id}")
    public ExtendedAd getInfoAboutAd(@PathVariable Integer id) {
        return adService.getInfoAboutAd(id);
    }

    /**
     * Удаление объявления
     */
    @Operation(summary = "Удаление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @DeleteMapping("{id}")
    public void deleteAd(@PathVariable int id) {
        adService.deleteAd(id);
    }

    /**
     * Обновление информации об объявлении
     */
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @PatchMapping("{id}")
    public CreateOrUpdateAd editInfoAboutAd(@PathVariable int id, @RequestBody CreateOrUpdateAd ad) {
        return adService.editInfoAboutAd(id, ad);
    }

    /**
     * Получение объявлений авторизованного пользователя
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @GetMapping("/me")
    public Ads receiveAdsAuthorizeUser(@RequestParam String userName) {
//        return adService.receiveAdsAuthorizeUser();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Ads ads = adService.getAdsForUser(userDetails.getUsername());
        return adService.receiveAdsAuthorizeUser(userName);
    }

    /**
     * Обновление картинки объявления
     */
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean renewImageAd(@PathVariable int id,
                                @RequestParam("image") MultipartFile imageFile) throws IOException {
//        adService.renewPhoto(id, photo);
        return adService.renewImageAd(id, imageFile);
    }
}
