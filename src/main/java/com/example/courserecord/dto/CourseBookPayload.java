package com.example.courserecord.dto;

import jakarta.validation.constraints.NotNull;

public record CourseBookPayload(@NotNull Long courseId, @NotNull Long bookId) {}
