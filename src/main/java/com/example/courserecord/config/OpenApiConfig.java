package com.example.courserecord.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearer-jwt";

    @Bean
    public OpenAPI courseRecordOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Course Record API").version("v1"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_SCHEME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("JWT authentication")))
                .tags(
                        List.of(
                                new Tag().name("Auth").description("Authentication"),
                                new Tag().name("Admin — Professors").description("Professor CRUD"),
                                new Tag().name("Admin — Students").description("Student CRUD"),
                                new Tag().name("Admin — Courses").description("Course CRUD"),
                                new Tag()
                                        .name("Admin — Course semesters")
                                        .description(
                                                "Course offering per study-program semester (1–8: two semesters per year over four years)"),
                                new Tag().name("Admin — Enrollments").description("Enrollment CRUD"),
                                new Tag().name("Admin — Exams").description("Exam CRUD"),
                                new Tag().name("Admin — Authors").description("Author CRUD"),
                                new Tag().name("Admin — Books").description("Book CRUD"),
                                new Tag().name("Admin — Course books").description("Course–book link CRUD"),
                                new Tag().name("Public").description("Public read-only catalog")));
    }
}
