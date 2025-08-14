package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static ru.skypro.homework.dto.Constants.*;

/**
 * Контроллер для работы с объявлениями
 *
 * @author Svetlana Ryazanova, 2025
 * @version 0.0.1
 */

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
    public ResponseEntity<Ads> getAllAds() {
//        return adService.getAllAds;
        return ResponseEntity.ok(CONSTANT_ADS);
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
    public ResponseEntity<Ad> addNewAds(@RequestPart("properties") CreateOrUpdateAd properties,
                                        @RequestPart("image") MultipartFile image,
                                        Authentication authentication) {
//        return adService.addNewAd(ad).getId;
        return ResponseEntity.ok(AD1);
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
    public ResponseEntity<ExtendedAd> getInfoAboutAd(@PathVariable Integer id) {
//        return adService.getInfoAboutAd(id);
        return ResponseEntity.ok(EXTENDED_AD);
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
    public ResponseEntity<Void> deleteAd(@PathVariable int id) {
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<CreateOrUpdateAd> editInfoAboutAd(@PathVariable int id, @RequestBody CreateOrUpdateAd ad) {
//        return adService.editAd(ad);
        return ResponseEntity.ok(ad);
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
    public ResponseEntity<Ads> receiveAdsAuthorizeUser() {
//        return adService.receiveAdsAuthorizeUser();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Ads ads = adService.getAdsForUser(userDetails.getUsername());
        return ResponseEntity.ok(CONSTANT_ADS);
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
    public ResponseEntity<String> renewImageAd(@PathVariable int id,
                                               @RequestParam("image") MultipartFile imageFile) throws IOException {
//        adService.renewPhoto(id, photo);
        return ResponseEntity.ok("Изображение успешно обновлено");
    }


}
