package com.example.courserecord.dto.catalog;

import java.util.List;

public record CatalogCourseDto(
        Long id,
        String code,
        String title,
        String description,
        int espb,
        CatalogProfessorDto professor,
        List<CatalogCourseSemesterDto> semesters,
        List<CatalogBookDto> books) {}
