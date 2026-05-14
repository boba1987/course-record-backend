package com.example.courserecord.repository;

import com.example.courserecord.entity.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(
            "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.professor "
                    + "LEFT JOIN FETCH c.semesters "
                    + "LEFT JOIN FETCH c.courseBooks cb "
                    + "LEFT JOIN FETCH cb.book b "
                    + "LEFT JOIN FETCH b.author")
    List<Course> findAllForPublicCatalog();

    @Query(
            "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.professor "
                    + "LEFT JOIN FETCH c.semesters "
                    + "LEFT JOIN FETCH c.courseBooks cb "
                    + "LEFT JOIN FETCH cb.book b "
                    + "LEFT JOIN FETCH b.author "
                    + "WHERE c.id = :id")
    Optional<Course> findByIdForPublicCatalog(@Param("id") Long id);

    boolean existsByCode(String code);
}
