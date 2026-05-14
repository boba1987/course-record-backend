package com.example.courserecord.dto;

import java.time.LocalDate;

public record BookDto(Long id, String title, LocalDate publicationDate, Long authorId) {}
