package com.example.courserecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookPayload(@NotBlank String title, @NotNull LocalDate publicationDate, Long authorId) {}
