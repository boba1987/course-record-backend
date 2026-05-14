package com.example.courserecord.jpa;

import com.example.courserecord.entity.Author;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class AuthorSpecifications {

    private AuthorSpecifications() {}

    public static Specification<Author> withOptionalNameFilters(String firstName, String lastName) {
        return (root, query, cb) -> {
            List<Predicate> parts = new ArrayList<>();
            LikePatterns.contains(firstName).ifPresent(pat -> parts.add(cb.like(cb.lower(root.get("firstName")), pat)));
            LikePatterns.contains(lastName).ifPresent(pat -> parts.add(cb.like(cb.lower(root.get("lastName")), pat)));
            if (parts.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(parts.toArray(Predicate[]::new));
        };
    }
}
