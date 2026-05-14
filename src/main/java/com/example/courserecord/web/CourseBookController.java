package com.example.courserecord.web;

import com.example.courserecord.dto.CourseBookDto;
import com.example.courserecord.dto.CourseBookPayload;
import com.example.courserecord.service.CourseBookService;
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
@RequestMapping("/api/course-books")
@Tag(name = "Admin — Course books")
@SecurityRequirement(name = "bearer-jwt")
public class CourseBookController {

    private final CourseBookService courseBookService;

    public CourseBookController(CourseBookService courseBookService) {
        this.courseBookService = courseBookService;
    }

    @GetMapping
    @Operation(summary = "List course–book links (paginated)")
    public Page<CourseBookDto> list(
            @Parameter(description = "Substring match on linked course title or code (case-insensitive)")
                    @RequestParam(required = false)
                    String courseName,
            @Parameter(description = "Substring match on linked book title (case-insensitive)")
                    @RequestParam(required = false)
                    String bookName,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return courseBookService.findAll(pageable, courseName, bookName);
    }

    @GetMapping("/{id}")
    public CourseBookDto get(@PathVariable Long id) {
        return courseBookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseBookDto> create(@Valid @RequestBody CourseBookPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseBookService.create(payload));
    }

    @PutMapping("/{id}")
    public CourseBookDto update(@PathVariable Long id, @Valid @RequestBody CourseBookPayload payload) {
        return courseBookService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseBookService.delete(id);
    }
}
