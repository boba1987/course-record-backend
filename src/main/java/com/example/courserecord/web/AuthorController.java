package com.example.courserecord.web;

import com.example.courserecord.dto.AuthorDto;
import com.example.courserecord.dto.AuthorPayload;
import com.example.courserecord.service.AuthorService;
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
@RequestMapping("/api/authors")
@Tag(name = "Admin — Authors")
@SecurityRequirement(name = "bearer-jwt")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "List authors (paginated)")
    public Page<AuthorDto> list(
            @Parameter(description = "Substring match on first name (case-insensitive)")
                    @RequestParam(required = false)
                    String firstName,
            @Parameter(description = "Substring match on last name (case-insensitive)")
                    @RequestParam(required = false)
                    String lastName,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return authorService.findAll(pageable, firstName, lastName);
    }

    @GetMapping("/{id}")
    public AuthorDto get(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<AuthorDto> create(@Valid @RequestBody AuthorPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(payload));
    }

    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable Long id, @Valid @RequestBody AuthorPayload payload) {
        return authorService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
