package ru.skypro.homework.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Comments getComments(Integer id) {

        List<Comment> comments = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("объявление не найдено"))
                .getComments()
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        return new Comments(comments.size(), comments);
    }

    /**
     * @param id
     * @param comment
     * @return
     */
    @Override
    public Comment addComment(Integer id, @NotNull CreateOrUpdateComment comment, @NotNull Authentication auth) {

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

        commentRepository.save(entity);

        return comment1;
    }

    /**
     * @param adId
     * @param commentId
     * @return
     */
    @Override
    public boolean deleteComment(Integer adId, Integer commentId) {
        AdEntity ad = adRepository.findById(adId).orElseThrow(() -> new NoSuchElementException("объявление не найдено"));
        CommentEntity comment = ad.getComments().stream()
                .filter(c -> c.getPk().equals(commentId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("комментарий не найден"));
        log.info("Удаляется комментарий{}", comment.getText());
        commentRepository.deleteById(comment.getPk());
        return true;
    }

    /**
     * @param adId
     * @param commentId
     * @param comment
     * @return
     */
    @Override
    public Comment updateComment(Integer adId, Integer commentId, @NotNull CreateOrUpdateComment comment) {
        AdEntity ad = adRepository.findById(adId).orElseThrow(() -> new NoSuchElementException("объявление не найдено"));
        CommentEntity comment1 = ad.getComments().stream()
                .filter(c -> c.getPk().equals(commentId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("комментарий не найден"));
        comment1.setText(comment.getText());
        commentRepository.save(comment1);
        return commentMapper.toDto(comment1);
    }
}
