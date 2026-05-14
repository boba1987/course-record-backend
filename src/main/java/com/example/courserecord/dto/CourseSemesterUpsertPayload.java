package com.example.courserecord.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CourseSemesterUpsertPayload(
        @NotNull Long courseId, @NotNull @Min(1) @Max(8) Integer semester) {}
