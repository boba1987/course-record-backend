package com.example.courserecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CoursePayload(
        @NotBlank String code,
        @NotBlank String title,
        @Size(max = 8000) String description,
        @NotNull @Positive Integer espb,
        Long professorId,
        List<CourseSemesterPayload> semesters) {}
