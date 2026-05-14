package com.example.courserecord.dto;

import jakarta.validation.constraints.NotBlank;

public record StudentPayload(@NotBlank String indexNumber, @NotBlank String firstName, @NotBlank String lastName) {}
