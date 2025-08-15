package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

@SpringBootTest
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    // Тест маппинга CommentEntity в Comment
    @Test
    public void convertCommentEntityToComment() {

        UserEntity author = new UserEntity();
        author.setId(1);
        author.setImage("image");
        author.setFirstName("Jon");
        CommentEntity commentEntity = new CommentEntity(1, 10L, "text", author, new AdEntity());

        Comment dto = commentMapper.toDto(commentEntity);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(commentEntity.getText(), dto.getText());
        Assertions.assertEquals(commentEntity.getPk(), dto.getPk());
        Assertions.assertEquals(commentEntity.getCreatedAt(), dto.getCreatedAt());
        Assertions.assertEquals(commentEntity.getAuthor().getId(), dto.getAuthor());
        Assertions.assertEquals(commentEntity.getAuthor().getImage(), dto.getAuthorImage());
        Assertions.assertEquals(commentEntity.getAuthor().getFirstName(), dto.getAuthorFirstName());
    }

    // Тест маппинга Comment в CommentEntity
    @Test
    public void convertCommentToCommentEntity() {

        Comment comment = new Comment();

        comment.setCreatedAt(100L);
        comment.setText("text");

        CommentEntity commentEntity = commentMapper.toEntity(comment);

        Assertions.assertNotNull(commentEntity);
        Assertions.assertEquals(comment.getCreatedAt(), commentEntity.getCreatedAt());
        Assertions.assertEquals(comment.getText(), commentEntity.getText());

    }

    // Тест маппинга CreateOrUpdateComment в CommentEntity
    @Test
    public void convertCreateOrUpdateCommentToCommentEntity() {

        CreateOrUpdateComment comment = new CreateOrUpdateComment("text");

        CommentEntity commentEntity = commentMapper.toEntity(comment);

        Assertions.assertNotNull(commentEntity);
        Assertions.assertEquals(comment.getText(), commentEntity.getText());
    }
}
