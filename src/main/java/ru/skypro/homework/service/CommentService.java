package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {

    Comments getComments(Integer id);

    Comment addComment(Integer id, CreateOrUpdateComment comment, Authentication auth);

    boolean deleteComment(Integer adId, Integer commentId);

    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment);
}
