package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Тестирование CommentService
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;
    @Mock
    private AdEntity adEntity;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Альтернатива extendWith, если предпочитаешь явную инициализацию
    }

    // Тест для метода getComments(). Для существующего объявления
    @Test
    void testGetCommentsSuccess() {
        Integer adPk = 1;

        AdEntity ad = new AdEntity();
        ad.setPk(adPk);
        ad.setComments(new ArrayList<>());

        CommentEntity comment1 = new CommentEntity();
        comment1.setPk(101);
        comment1.setText("First comment");
        comment1.setAd(ad);

        CommentEntity comment2 = new CommentEntity();
        comment2.setPk(102);
        comment2.setText("Second comment");
        comment2.setAd(ad);

        ad.getComments().addAll(Arrays.asList(comment1, comment2));

        when(adRepository.findById(adPk)).thenReturn(Optional.of(ad));

        when(commentRepository.findAll()).thenReturn(List.of(comment1, comment2));

        when(commentMapper.toDto(comment1)).thenReturn(new Comment());
        when(commentMapper.toDto(comment2)).thenReturn(new Comment());

        Comments result = commentService.getComments(adPk);

        assertNotNull(result);
        assertEquals(2, result.getCount());
    }

    // Тест для метода getComments(). Для отсутствующего объявления
    @Test
    void testGetCommentsAdNotFound() {
        Integer adId = -999;

        when(adRepository.findById(adId)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            commentService.getComments(adId);
        });

        verify(adRepository).findById(adId);
    }

    // Тест для метода addComment(). Для существующего объявления
    @Test
    void testAddCommentSuccess() {
        Integer adId = 1;
        String username = "testUser";

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        UserEntity user = new UserEntity();
        user.setId(777);
        user.setFirstName("FirstName");
        user.setImage("imageUrl");

        when(userRepository.findByUsername(username)).thenReturn(user);

        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("Test comment");

        Comment commentDto = new Comment();

        CommentEntity commentEntity = new CommentEntity();

        when(commentMapper.toEntity(any(Comment.class))).thenReturn(commentEntity);

        AdEntity ad = new AdEntity();
        ad.setPk(adId);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        when(commentRepository.save(any(CommentEntity.class))).thenReturn(new CommentEntity());

        Comment result = commentService.addComment(adId, createComment, auth);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());

        verify(commentRepository).save(any(CommentEntity.class));
    }

    // Тест для метода addComment(). Для отсутствующего объявления
    @Test
    void testAddCommentAdNotFound() {
        CommentEntity mockCommentEntity = new CommentEntity();
        when(commentMapper.toEntity(any(Comment.class))).thenReturn(mockCommentEntity);

        Integer adId = 1;
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("Test comment");
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user");

        UserEntity user = new UserEntity();
        user.setId(10);
        user.setFirstName("FirstName");
        user.setImage("imageUrl");
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> commentService.addComment(adId, createComment, auth),
                "NoSuchElementException"
        );

        assertEquals("объявление не найдено", thrown.getMessage());
    }

    // Тест для метода deleteComment(). Для существующего комментария
    @Test
    void testDeleteCommentSuccess() {
        Integer adId = 1;
        Integer commentId = 5;

        CommentEntity comment = new CommentEntity();
        comment.setPk(commentId);
        comment.setText("Test comment");

        AdEntity ad = new AdEntity();
        ad.setPk(adId);
        ad.setComments(new ArrayList<>(Arrays.asList(comment)));

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        doNothing().when(commentRepository).deleteById(commentId);

        boolean result = commentService.deleteComment(adId, commentId);

        assertTrue(result);

        verify(commentRepository).deleteById(commentId);
    }

    // Тест для метода deleteComment(). Для отсутствующего комментария
    @Test
    void testDeleteCommentCommentNotFound() {
        Integer adId = 1;
        Integer commentId = -999;

        CommentEntity comment = new CommentEntity();
        comment.setPk(100);
        comment.setText("Test comment1");

        AdEntity ad = new AdEntity();
        ad.setPk(adId);
        ad.setComments(new ArrayList<>(Arrays.asList(comment)));

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> commentService.deleteComment(adId, commentId),
                "NoSuchElementException"
        );
        assertEquals("комментарий не найден", thrown.getMessage());
    }


    // Тест для метода updateComment(). Для отсутствующего объявления
    @Test
    void testUpdateCommentAdNotFound() {
        Integer adId = -999;
        Integer commentId = 2;

        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        CreateOrUpdateComment updateDto = new CreateOrUpdateComment();
        updateDto.setText("текст");

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            commentService.updateComment(adId, commentId, updateDto);
        });

        assertEquals("объявление не найдено", exception.getMessage());
    }

    // Тест для метода updateComment(). Для отсутствующего комментария
    @Test
    void testUpdateCommentCommentNotFound() {
        Integer adId = 1;
        Integer commentId = -555;

        AdEntity adEntity = new AdEntity();

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        adEntity.setComments(Arrays.asList());

        CreateOrUpdateComment updateDto = new CreateOrUpdateComment();
        updateDto.setText("текст");

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            commentService.updateComment(adId, commentId, updateDto);
        });

        assertEquals("комментарий не найден", exception.getMessage());
    }
}