package com.example.courserecord.jpa;

import com.example.courserecord.entity.CourseBook;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CourseBookSpecifications {

    private CourseBookSpecifications() {}

    public static Specification<CourseBook> withOptionalFilters(String courseName, String bookName) {
        return (root, query, cb) -> {
            List<Predicate> parts = new ArrayList<>();
            LikePatterns.contains(courseName)
                    .ifPresent(pat -> {
                        var course = root.join("course", JoinType.INNER);
                        parts.add(cb.or(
                                cb.like(cb.lower(course.get("title")), pat),
                                cb.like(cb.lower(course.get("code")), pat)));
                    });
            LikePatterns.contains(bookName).ifPresent(pat -> {
                var book = root.join("book", JoinType.INNER);
                parts.add(cb.like(cb.lower(book.get("title")), pat));
            });
            if (parts.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(parts.toArray(Predicate[]::new));
        };
    }
}
