package com.example.courserecord.dto;

import java.util.List;

public record CourseDto(
        Long id, String code, String title, int espb, Long professorId, List<CourseSemesterDto> semesters) {}
