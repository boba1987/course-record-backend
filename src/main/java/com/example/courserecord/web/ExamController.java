package com.example.courserecord.web;

import com.example.courserecord.dto.ExamDto;
import com.example.courserecord.dto.ExamPayload;
import com.example.courserecord.service.ExamService;
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
@RequestMapping("/api/exams")
@Tag(name = "Admin — Exams")
@SecurityRequirement(name = "bearer-jwt")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public List<ExamDto> list() {
        return examService.findAll();
    }

    @GetMapping("/{id}")
    public ExamDto get(@PathVariable Long id) {
        return examService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ExamDto> create(@Valid @RequestBody ExamPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.create(payload));
    }

    @PutMapping("/{id}")
    public ExamDto update(@PathVariable Long id, @Valid @RequestBody ExamPayload payload) {
        return examService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        examService.delete(id);
    }
}
