package com.example.courserecord.dto;

import java.time.LocalDate;

public record ExamDto(Long id, Long studentId, Long courseId, LocalDate examDate, int grade) {}
