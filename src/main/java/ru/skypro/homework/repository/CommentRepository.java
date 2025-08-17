package ru.skypro.homework.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query(value = "DELETE from комментарии where Id_комментария = :comm_id", nativeQuery = true)
    @Modifying
    void deleteByCommId(Integer comm_id);

}
