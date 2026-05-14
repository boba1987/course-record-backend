package com.example.courserecord.web;

import com.example.courserecord.dto.CourseSemesterDto;
import com.example.courserecord.dto.CourseSemesterUpsertPayload;
import com.example.courserecord.service.CourseSemesterService;
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
@RequestMapping("/api/course-semesters")
@Tag(name = "Admin — Course semesters")
@SecurityRequirement(name = "bearer-jwt")
public class CourseSemesterController {

    private final CourseSemesterService courseSemesterService;

    public CourseSemesterController(CourseSemesterService courseSemesterService) {
        this.courseSemesterService = courseSemesterService;
    }

    @GetMapping
    public List<CourseSemesterDto> list() {
        return courseSemesterService.findAll();
    }

    @GetMapping("/{id}")
    public CourseSemesterDto get(@PathVariable Long id) {
        return courseSemesterService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CourseSemesterDto> create(@Valid @RequestBody CourseSemesterUpsertPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseSemesterService.create(payload));
    }

    @PutMapping("/{id}")
    public CourseSemesterDto update(@PathVariable Long id, @Valid @RequestBody CourseSemesterUpsertPayload payload) {
        return courseSemesterService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseSemesterService.delete(id);
    }
}
