package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;

/**
 * Контроллер для работы с комментариями
 *
 * @author Lada Kozlova, 2025
 * @version 0.0.1
 */
@Tag(name = "Комментарии")
@RestController
@RequestMapping("/ads")
public class CommentController {
    /**
     * Получение комментариев объявления
     */
    @Operation(summary = "Получение комментариев объявления")
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(new Comments());
    }

    /**
     * Добавление комментария к объявлению
     */
    @Operation(summary = "Добавление комментария к объявлению")
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer id,
                                              @RequestBody Comment newComment) {
        return ResponseEntity.ok(newComment);
    }

    /**
     * Удаление комментария
     */
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") Integer adId,
                                              @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление комментария
     */
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") Integer adId,
                                                 @PathVariable("commentId") Integer commentId,
                                                 @RequestBody Comment updatedComment) {
        return ResponseEntity.ok(updatedComment);
    }
}
