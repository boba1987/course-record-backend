package com.example.courserecord.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseSemesterPayload(@NotNull @Positive Integer academicYear, @NotNull @Positive Integer semester) {}
