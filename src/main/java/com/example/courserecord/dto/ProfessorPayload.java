package com.example.courserecord.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfessorPayload(@NotBlank String firstName, @NotBlank String lastName) {}
