package com.example.courserecord.web;

import com.example.courserecord.dto.BookDto;
import com.example.courserecord.dto.BookPayload;
import com.example.courserecord.service.BookService;
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
@RequestMapping("/api/books")
@Tag(name = "Admin — Books")
@SecurityRequirement(name = "bearer-jwt")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "List books (paginated)")
    public Page<BookDto> list(
            @Parameter(description = "Substring match on book title (case-insensitive)")
                    @RequestParam(required = false)
                    String title,
            @Parameter(description = "Substring match on author first name (case-insensitive)")
                    @RequestParam(required = false)
                    String authorFirstName,
            @Parameter(description = "Substring match on author last name (case-insensitive)")
                    @RequestParam(required = false)
                    String authorLastName,
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return bookService.findAll(pageable, title, authorFirstName, authorLastName);
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@Valid @RequestBody BookPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(payload));
    }

    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id, @Valid @RequestBody BookPayload payload) {
        return bookService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
