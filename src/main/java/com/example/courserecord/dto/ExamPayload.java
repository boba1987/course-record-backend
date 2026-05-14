package com.example.courserecord.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExamPayload(
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull LocalDate examDate,
        @NotNull @Min(5) @Max(10) Integer grade) {}
