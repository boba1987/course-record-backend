package com.example.courserecord.web;

import com.example.courserecord.dto.catalog.CatalogAuthorDto;
import com.example.courserecord.dto.catalog.CatalogBookDto;
import com.example.courserecord.dto.catalog.CatalogCourseDto;
import com.example.courserecord.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public")
public class PublicCatalogController {

    private final CatalogService catalogService;

    public PublicCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/courses")
    @Operation(summary = "List courses (paginated)")
    public Page<CatalogCourseDto> courses(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return catalogService.listCourses(pageable);
    }

    @GetMapping("/books")
    @Operation(summary = "List books (paginated)")
    public Page<CatalogBookDto> books(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return catalogService.listBooks(pageable);
    }

    @GetMapping("/authors")
    @Operation(summary = "List authors (paginated)")
    public Page<CatalogAuthorDto> authors(
            @ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return catalogService.listAuthors(pageable);
    }
}
