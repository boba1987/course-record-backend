package com.example.courserecord.dto.catalog;

import java.time.LocalDate;

public record CatalogBookDto(Long id, String title, LocalDate publicationDate, CatalogAuthorDto author) {}
