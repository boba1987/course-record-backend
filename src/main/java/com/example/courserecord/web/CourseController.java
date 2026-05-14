package com.example.courserecord.web;

import com.example.courserecord.dto.CourseDto;
import com.example.courserecord.dto.CoursePayload;
import com.example.courserecord.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Admin — Courses")
@SecurityRequirement(name = "bearer-jwt")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "List courses (paginated)")
    public Page<CourseDto> list(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return courseService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public CourseDto get(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(@Valid @RequestBody CoursePayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(payload));
    }

    @PutMapping("/{id}")
    public CourseDto update(@PathVariable Long id, @Valid @RequestBody CoursePayload payload) {
        return courseService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
