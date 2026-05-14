package com.example.courserecord.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentPayload(@NotNull Long studentId, @NotNull Long courseId) {}
