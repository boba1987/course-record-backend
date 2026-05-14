package com.example.courserecord.jpa;

import com.example.courserecord.entity.Book;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {}

    public static Specification<Book> withOptionalFilters(String title, String authorFirstName, String authorLastName) {
        return (root, query, cb) -> {
            List<Predicate> parts = new ArrayList<>();
            LikePatterns.contains(title).ifPresent(pat -> parts.add(cb.like(cb.lower(root.get("title")), pat)));
            var authorFirst = LikePatterns.contains(authorFirstName);
            var authorLast = LikePatterns.contains(authorLastName);
            if (authorFirst.isPresent() || authorLast.isPresent()) {
                var author = root.join("author", JoinType.INNER);
                List<Predicate> authorParts = new ArrayList<>();
                authorFirst.ifPresent(pat -> authorParts.add(cb.like(cb.lower(author.get("firstName")), pat)));
                authorLast.ifPresent(pat -> authorParts.add(cb.like(cb.lower(author.get("lastName")), pat)));
                parts.add(cb.and(authorParts.toArray(Predicate[]::new)));
            }
            if (parts.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(parts.toArray(Predicate[]::new));
        };
    }
}
