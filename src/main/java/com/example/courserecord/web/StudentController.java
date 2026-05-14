package com.example.courserecord.web;

import com.example.courserecord.dto.StudentDto;
import com.example.courserecord.dto.StudentPayload;
import com.example.courserecord.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Admin — Students")
@SecurityRequirement(name = "bearer-jwt")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "List students (paginated)")
    public Page<StudentDto> list(
            @Parameter(description = "Substring match on first name (case-insensitive)")
                    @RequestParam(required = false)
                    String firstName,
            @Parameter(description = "Substring match on last name (case-insensitive)")
                    @RequestParam(required = false)
                    String lastName,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return studentService.findAll(pageable, firstName, lastName);
    }

    @GetMapping("/{id}")
    public StudentDto get(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudentDto> create(@Valid @RequestBody StudentPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(payload));
    }

    @PutMapping("/{id}")
    public StudentDto update(@PathVariable Long id, @Valid @RequestBody StudentPayload payload) {
        return studentService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
