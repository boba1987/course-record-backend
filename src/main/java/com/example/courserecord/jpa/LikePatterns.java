package com.example.courserecord.jpa;

import java.util.Locale;
import java.util.Optional;
import org.springframework.util.StringUtils;

/** Lowercase {@code %value%} patterns for case-insensitive LIKE filters. */
public final class LikePatterns {

    private LikePatterns() {}

    /** Trims input; empty → empty Optional. */
    public static Optional<String> contains(String raw) {
        if (!StringUtils.hasText(raw)) {
            return Optional.empty();
        }
        return Optional.of("%" + raw.trim().toLowerCase(Locale.ROOT) + "%");
    }
}
