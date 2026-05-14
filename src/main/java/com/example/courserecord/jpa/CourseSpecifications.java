package com.example.courserecord.jpa;

import com.example.courserecord.entity.Course;
import org.springframework.data.jpa.domain.Specification;

public final class CourseSpecifications {

    private CourseSpecifications() {}

    /** Matches {@code title} or {@code code} (case-insensitive contains). */
    public static Specification<Course> nameContains(String name) {
        return (root, query, cb) -> {
            var pat = LikePatterns.contains(name);
            if (pat.isEmpty()) {
                return cb.conjunction();
            }
            String p = pat.get();
            return cb.or(
                    cb.like(cb.lower(root.get("title")), p),
                    cb.like(cb.lower(root.get("code")), p));
        };
    }
}
