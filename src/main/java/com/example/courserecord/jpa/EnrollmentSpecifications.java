package com.example.courserecord.jpa;

import com.example.courserecord.entity.Enrollment;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class EnrollmentSpecifications {

    private EnrollmentSpecifications() {}

    /**
     * Optional {@code student}: matches student first name, last name, or index number (contains, case-insensitive).
     * Optional {@code course}: matches course title or code (contains, case-insensitive). Both filters are ANDed.
     */
    public static Specification<Enrollment> withOptionalFilters(String student, String course) {
        return (root, query, cb) -> {
            List<Predicate> parts = new ArrayList<>();
            LikePatterns.contains(student).ifPresent(pat -> {
                var s = root.join("student", JoinType.INNER);
                parts.add(cb.or(
                        cb.like(cb.lower(s.get("firstName")), pat),
                        cb.like(cb.lower(s.get("lastName")), pat),
                        cb.like(cb.lower(s.get("indexNumber")), pat)));
            });
            LikePatterns.contains(course).ifPresent(pat -> {
                var c = root.join("course", JoinType.INNER);
                parts.add(cb.or(
                        cb.like(cb.lower(c.get("title")), pat),
                        cb.like(cb.lower(c.get("code")), pat)));
            });
            if (parts.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(parts.toArray(Predicate[]::new));
        };
    }
}
