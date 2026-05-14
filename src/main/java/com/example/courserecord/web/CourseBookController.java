package com.example.courserecord.web;

import com.example.courserecord.dto.CourseBookDto;
import com.example.courserecord.dto.CourseBookPayload;
import com.example.courserecord.service.CourseBookService;
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
@RequestMapping("/api/course-books")
@Tag(name = "Admin — Course books")
@SecurityRequirement(name = "bearer-jwt")
public class CourseBookController {

    private final CourseBookService courseBookService;

    public CourseBookController(CourseBookService courseBookService) {
        this.courseBookService = courseBookService;
    }

    @GetMapping
    public List<CourseBookDto> list() {
        return courseBookService.findAll();
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
