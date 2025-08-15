package ru.skypro.homework.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    @Modifying
    void deleteById(@NotNull Integer id);
}
