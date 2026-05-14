package com.example.courserecord.web;

import com.example.courserecord.dto.catalog.CatalogAuthorDto;
import com.example.courserecord.dto.catalog.CatalogBookDto;
import com.example.courserecord.dto.catalog.CatalogCourseDto;
import com.example.courserecord.service.CatalogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
    public List<CatalogCourseDto> courses() {
        return catalogService.listCourses();
    }

    @GetMapping("/books")
    public List<CatalogBookDto> books() {
        return catalogService.listBooks();
    }

    @GetMapping("/authors")
    public List<CatalogAuthorDto> authors() {
        return catalogService.listAuthors();
    }
}
