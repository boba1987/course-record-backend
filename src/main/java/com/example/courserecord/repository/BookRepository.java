package com.example.courserecord.repository;

import com.example.courserecord.entity.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b.id FROM Book b", countQuery = "SELECT count(b) FROM Book b")
    Page<Long> findIdsForPublicCatalog(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author WHERE b.id IN :ids")
    List<Book> findAllWithAuthorByIdIn(@Param("ids") Collection<Long> ids);
}
