package com.example.courserecord.repository;

import com.example.courserecord.entity.Course;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query(value = "SELECT c.id FROM Course c", countQuery = "SELECT count(c) FROM Course c")
    Page<Long> findIdsForPublicCatalog(Pageable pageable);

    /**
     * Loads professor and semesters only for the given ids. Course books are loaded in a separate query to avoid
     * {@code MultipleBagFetchException} (two {@code List} collections on {@link Course}).
     */
    @Query(
            "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.professor "
                    + "LEFT JOIN FETCH c.semesters "
                    + "WHERE c.id IN :ids")
    List<Course> findAllForPublicCatalogWithProfessorAndSemestersByIdIn(@Param("ids") Collection<Long> ids);

    @Query(
            "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.courseBooks cb "
                    + "LEFT JOIN FETCH cb.book b "
                    + "LEFT JOIN FETCH b.author "
                    + "WHERE c.id IN :ids")
    List<Course> findAllForPublicCatalogWithBooks(@Param("ids") Collection<Long> ids);

    boolean existsByCode(String code);
}
