package com.example.courserecord.repository;

import com.example.courserecord.entity.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(
            "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.professor "
                    + "LEFT JOIN FETCH c.semesters "
                    + "LEFT JOIN FETCH c.courseBooks cb "
                    + "LEFT JOIN FETCH cb.book b "
                    + "LEFT JOIN FETCH b.author")
    List<Course> findAllForPublicCatalog();

    boolean existsByCode(String code);
}
