package ru.skypro.homework.service.impl;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.SecurityUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper, UserRepository userRepository, SecurityUtils securityUtils) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    /**
     * Метод для получения комментарев по объявлению
     *
     * @param id - Id объявления
     * @return возвращает список комментариев объявления
     */
    @Override
    public Comments getComments(Integer id) {

        logger.info("Method for get Comments");

        List<Comment> comments = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("объявление не найдено"))
                .getComments()
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        return new Comments(comments.size(), comments);
    }

    /**
     * Метод для добавления нового комментария
     *
     * @param id - Id объявления
     * @param comment - DTO для добавления или обновления комментария.
     * @return возвращает добавленный комментарий
     */

    @Override
    public Comment addComment(Integer id, @NotNull CreateOrUpdateComment comment, @NotNull Authentication auth) {

        logger.info("Method for add new Comment");

        UserEntity user = userRepository.findByUsername(auth.getName());
        Comment comment1 = new Comment();
        comment1.setText(comment.getText());
        comment1.setCreatedAt(System.currentTimeMillis());
        comment1.setAuthor(user.getId());
        comment1.setAuthorImage(user.getImage());
        comment1.setAuthorFirstName(user.getFirstName());

        CommentEntity entity = commentMapper.toEntity(comment1);
        entity.setAuthor(user);
        entity.setAd(adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("объявление не найдено")));

        CommentEntity entity1 = commentRepository.save(entity);

        return commentMapper.toDto(entity1);
    }

    /**
     * Метод для удаления комментария
     *
     * @param adId - Id объявления
     * @param commentId -Id комментария
     * @return boolean
     */
    @Override
    @Transactional
    public boolean deleteComment(Integer adId, Integer commentId) {

        logger.info("Method for delete Comment");

        AdEntity ad = adRepository.findById(adId).orElseThrow(() -> new NoSuchElementException("объявление не найдено"));
        CommentEntity comment = ad.getComments().stream()
                .filter(c -> c.getPk().equals(commentId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("комментарий не найден"));
        User currentUser = securityUtils.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            if (!(commentRepository.getReferenceById(commentId).getAuthor().getId()).equals(currentUser.getId())) {
                throw new AccessDeniedException("У вас нет прав на редактирование этого объявления");
            }
        }
        log.info("Удаляется комментарий id={}", comment.getPk());
        commentRepository.deleteByCommId(comment.getPk());
        return true;
    }

    /**
     * Метод для обновления комментария
     *
     * @param adId - Id объявления
     * @param commentId - Id комментария
     * @param comment - DTO для добавления или обновления комментария.
     * @return возвращает сохраненный комментарий
     */

    @Override
    public Comment updateComment(Integer adId, Integer commentId, @NotNull CreateOrUpdateComment comment) {

        logger.info("Method for update Comment");

        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new NoSuchElementException("объявление не найдено"));

        CommentEntity comment1 = ad.getComments().stream()
                .filter(c -> c.getPk().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("комментарий не найден"));

        User currentUser = securityUtils.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            log.info("не админ");
            if (commentRepository.getReferenceById(commentId).getAuthor().getId() != currentUser.getId()) {
                log.info("не автор");
                throw new AccessDeniedException("У вас нет прав на редактирование этого объявления");
            }
        }
        comment1.setText(comment.getText());
        commentRepository.save(comment1);
        return commentMapper.toDto(comment1);
    }
}
