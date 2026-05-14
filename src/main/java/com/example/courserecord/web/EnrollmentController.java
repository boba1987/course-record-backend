package com.example.courserecord.web;

import com.example.courserecord.dto.EnrollmentDto;
import com.example.courserecord.dto.EnrollmentPayload;
import com.example.courserecord.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/enrollments")
@Tag(name = "Admin — Enrollments")
@SecurityRequirement(name = "bearer-jwt")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    @Operation(summary = "List enrollments (paginated)")
    public Page<EnrollmentDto> list(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return enrollmentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public EnrollmentDto get(@PathVariable Long id) {
        return enrollmentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> create(@Valid @RequestBody EnrollmentPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.create(payload));
    }

    @PutMapping("/{id}")
    public EnrollmentDto update(@PathVariable Long id, @Valid @RequestBody EnrollmentPayload payload) {
        return enrollmentService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        enrollmentService.delete(id);
    }
}
