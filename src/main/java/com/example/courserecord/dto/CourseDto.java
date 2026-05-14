package com.example.courserecord.dto;

import java.util.List;

public record CourseDto(
        Long id,
        String code,
        String title,
        String description,
        int espb,
        Long professorId,
        List<CourseSemesterDto> semesters) {}
