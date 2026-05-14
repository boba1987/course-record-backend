package com.example.courserecord.web;

import com.example.courserecord.dto.ProfessorDto;
import com.example.courserecord.dto.ProfessorPayload;
import com.example.courserecord.service.ProfessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professors")
@Tag(name = "Admin — Professors")
@SecurityRequirement(name = "bearer-jwt")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<ProfessorDto> list() {
        return professorService.findAll();
    }

    @GetMapping("/{id}")
    public ProfessorDto get(@PathVariable Long id) {
        return professorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProfessorDto> create(@Valid @RequestBody ProfessorPayload payload) {
        ProfessorDto created = professorService.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ProfessorDto update(@PathVariable Long id, @Valid @RequestBody ProfessorPayload payload) {
        return professorService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        professorService.delete(id);
    }
}
