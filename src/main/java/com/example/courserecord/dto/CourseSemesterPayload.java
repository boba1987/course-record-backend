package com.example.courserecord.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Study-program semester 1–8 (two semesters per academic year × four years). */
public record CourseSemesterPayload(@NotNull @Min(1) @Max(8) Integer semester) {}
