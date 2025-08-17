package ru.skypro.homework.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Тестирование AdService
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
public class AdServiceTest {
    @Mock
    private AdRepository adRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdMapper adMapper;
    @Mock
    private MultipartFile imageFile;
    @InjectMocks
    private AdServiceImpl adService;

    private AdEntity adEntity;
    private ExtendedAd extendedAd;
    private CreateOrUpdateAd createOrUpdateAd;
    private UserEntity user;
    private List<AdEntity> adEntities;
    private List<Ad> adDtos;
    private final String imageDir = "/test/pathToAdds";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adEntity = new AdEntity();
        adEntity.setPk(1);
        adEntity.setTitle("Old Title");
        adEntity.setPrice(100);
        adEntity.setDescription("Old Description");

        createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("New Title");
        createOrUpdateAd.setPrice(200);
        createOrUpdateAd.setDescription("New Description");

        extendedAd = new ExtendedAd();

        user = new UserEntity();
        user.setId(1);
        user.setUsername("testUser");

        AdEntity ad1 = new AdEntity();
        ad1.setPk(101);
        AdEntity ad2 = new AdEntity();
        ad2.setPk(102);
        adEntities = Arrays.asList(ad1, ad2);

        Ad adDto1 = new Ad();
        adDto1.setPk(101);
        Ad adDto2 = new Ad();
        adDto2.setPk(102);
        adDtos = Arrays.asList(adDto1, adDto2);

        try {
            var field = AdServiceImpl.class.getDeclaredField("imageDir");
            field.setAccessible(true);
            field.set(adService, imageDir);
        } catch (Exception e) {
            fail("Failed to set imageDir");
        }
    }

    // Тест для метода getAllAds()
    @Test
    void testGetAllAds() {
        AdEntity adEntity1 = new AdEntity();
        adEntity1.setPk(1);
        AdEntity adEntity2 = new AdEntity();
        adEntity2.setPk(2);

        List<AdEntity> entities = Arrays.asList(adEntity1, adEntity2);

        when(adRepository.findAll()).thenReturn(entities);

        Ad dto1 = new Ad();
        Ad dto2 = new Ad();

        when(adMapper.toDto(adEntity1)).thenReturn(dto1);
        when(adMapper.toDto(adEntity2)).thenReturn(dto2);

        Ads result = adService.getAllAds();

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());
        assertTrue(result.getResults().contains(dto1));
        assertTrue(result.getResults().contains(dto2));

        verify(adRepository).findAll();
        verify(adMapper).toDto(adEntity1);
        verify(adMapper).toDto(adEntity2);
    }

    // Тест для метода addNewAds
    @Test
    void testAddNewAds() throws IOException {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("Test Title");
        properties.setPrice(100);
        properties.setDescription("Test Description");

        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());
        ReflectionTestUtils.setField(adService, "imageDir", "/tmp/uploads");
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        when(adRepository.save(any(AdEntity.class))).thenAnswer(invocation -> {
            AdEntity savedAd = invocation.getArgument(0);
            savedAd.setPk(123);
            return savedAd;
        });

        Ad expectedDto = new Ad();

        when(adMapper.toDto(any(AdEntity.class))).thenReturn(expectedDto);

        Ad result = adService.addNewAds(properties, imageFile, auth);

        assertNotNull(result);

        verify(userRepository).findByUsername("testuser");

        verify(adRepository).save(any(AdEntity.class));

        verify(adMapper).toDto(any(AdEntity.class));

        assertEquals(expectedDto, result);
    }

    // Тест для метода getInfoAboutAd
    @Test
    public void testGetInfoAboutAdReturnsExtendedAd() {
        when(adRepository.findById(1)).thenReturn(java.util.Optional.of(adEntity));
        when(adMapper.toExtendedDto(adEntity)).thenReturn(extendedAd);

        ExtendedAd result = adService.getInfoAboutAd(1);

        assertNotNull(result);
        assertEquals(extendedAd, result);
        verify(adRepository).findById(1);
        verify(adMapper).toExtendedDto(adEntity);
    }

    // Тест для метода getInfoAboutAd. Случай когда объявление не найдено
    @Test
    public void testGetInfoAboutAdAdNotFound() {
        when(adRepository.findById(2)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            adService.getInfoAboutAd(2);
        });
        verify(adRepository).findById(2);
    }

    // Тест для метода deleteAd
    @Test
    public void testDeleteAd() {
        adService.deleteAd(1);
        verify(adRepository).deleteById(1);
    }

    // Тест для метода editInfoAboutAd. Успешное редактирование
    @Test
    public void testEditInfoAboutAdSuccess() {
        when(adRepository.findById(1)).thenReturn(java.util.Optional.of(adEntity));
        when(adRepository.save(any(AdEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CreateOrUpdateAd updatedDto = new CreateOrUpdateAd();
        when(adMapper.toDtoAd(any(AdEntity.class))).thenReturn(updatedDto);

        CreateOrUpdateAd result = adService.editInfoAboutAd(1, createOrUpdateAd);

        assertNotNull(result);
        assertEquals("New Title", adEntity.getTitle());
        assertEquals(200, adEntity.getPrice());
        assertEquals("New Description", adEntity.getDescription());

        verify(adRepository).findById(1);
        verify(adRepository).save(adEntity);
        verify(adMapper).toDtoAd(adEntity);
    }

    // Тест для метода editInfoAboutAd. Случай когда объявление не найдено
    @Test
    public void testEditInfoAboutAdAdNotFound() {
        when(adRepository.findById(2)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            adService.editInfoAboutAd(2, createOrUpdateAd);
        });

        verify(adRepository).findById(2);
    }

    // Тест для метода receiveAdsAuthorizeUser
    @Test
    public void testReceiveAdsAuthorizeUser() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(adRepository.findAllAdsByAuthor(user)).thenReturn(adEntities);

        when(adMapper.toDto(any(AdEntity.class))).thenAnswer(invocation -> {
            AdEntity entity = invocation.getArgument(0);
            Ad dto = new Ad();
            dto.setPk(entity.getPk());
            return dto;
        });

        Ads resultAds = adService.receiveAdsAuthorizeUser(username);

        assertNotNull(resultAds);
        assertEquals(2, resultAds.getCount());
        assertEquals(2, resultAds.getResults().size());

        verify(adMapper, times(2)).toDto(any(AdEntity.class));

        assertEquals(Long.valueOf(101), resultAds.getResults().get(0).getPk());
        assertEquals(Long.valueOf(102), resultAds.getResults().get(1).getPk());
    }

    // Тест для метода renewImageAd. Успешное обновление
    @Test
    void testRenewImageAdSuccess() throws IOException {

        Integer adId = 1;
        AdEntity adEntity = new AdEntity();
        adEntity.setPk(adId);
        adEntity.setImage("/some/old/image.jpg");

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(imageFile.getOriginalFilename()).thenReturn("new_image.png");
        when(imageFile.getInputStream()).thenReturn(null);

        Path expectedPath = Path.of(imageDir, adEntity.hashCode() + ".png");

        boolean result = adService.renewImageAd(adId, imageFile);

        assertTrue(result);
        verify(adRepository).findById(adId);
        verify(adRepository).save(any(AdEntity.class));

        assertEquals(expectedPath.toString(), adEntity.getImage());

    }

    // Тест для метода renewImageAd. Случай когда объявление не найдено
    @Test
    void testRenewImageAdAdNotFound() {
        Integer adPk = 2;

        when(adRepository.findById(adPk)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            adService.renewImageAd(adPk, imageFile);
        });

        verify(adRepository).findById(adPk);
    }
}




