package com.example.courserecord.service;

import com.example.courserecord.dto.catalog.CatalogAuthorDto;
import com.example.courserecord.dto.catalog.CatalogBookDto;
import com.example.courserecord.dto.catalog.CatalogCourseDto;
import com.example.courserecord.dto.catalog.CatalogCourseSemesterDto;
import com.example.courserecord.dto.catalog.CatalogProfessorDto;
import com.example.courserecord.entity.Author;
import com.example.courserecord.entity.Book;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.CourseBook;
import com.example.courserecord.entity.CourseSemester;
import com.example.courserecord.entity.Professor;
import com.example.courserecord.repository.AuthorRepository;
import com.example.courserecord.repository.BookRepository;
import com.example.courserecord.repository.CourseRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final CourseRepository courseRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public CatalogService(
            CourseRepository courseRepository, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.courseRepository = courseRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<CatalogCourseDto> listCourses(Pageable pageable) {
        Page<Long> idPage = courseRepository.findIdsForPublicCatalog(pageable);
        if (idPage.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Long> ids = idPage.getContent();
        List<Course> courses = courseRepository.findAllForPublicCatalogWithProfessorAndSemestersByIdIn(ids);
        courseRepository.findAllForPublicCatalogWithBooks(ids);
        Map<Long, Course> byId = courses.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        List<CatalogCourseDto> content =
                ids.stream().map(byId::get).filter(Objects::nonNull).map(this::toCatalogCourse).toList();
        return new PageImpl<>(content, pageable, idPage.getTotalElements());
    }

    public Page<CatalogBookDto> listBooks(Pageable pageable) {
        Page<Long> idPage = bookRepository.findIdsForPublicCatalog(pageable);
        if (idPage.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Long> ids = idPage.getContent();
        List<Book> books = bookRepository.findAllWithAuthorByIdIn(ids);
        Map<Long, Book> byId = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        List<CatalogBookDto> content =
                ids.stream().map(byId::get).filter(Objects::nonNull).map(this::toCatalogBook).toList();
        return new PageImpl<>(content, pageable, idPage.getTotalElements());
    }

    public Page<CatalogAuthorDto> listAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(this::toCatalogAuthor);
    }

    private CatalogCourseDto toCatalogCourse(Course c) {
        CatalogProfessorDto prof = toCatalogProfessor(c.getProfessor());
        List<CatalogCourseSemesterDto> semesters =
                c.getSemesters().stream()
                        .sorted(Comparator.comparingInt(CourseSemester::getSemester))
                        .map(s -> new CatalogCourseSemesterDto(s.getId(), s.getSemester()))
                        .toList();
        List<CatalogBookDto> books =
                c.getCourseBooks().stream().map(CourseBook::getBook).map(this::toCatalogBook).toList();
        return new CatalogCourseDto(c.getId(), c.getCode(), c.getTitle(), c.getEspb(), prof, semesters, books);
    }

    private CatalogProfessorDto toCatalogProfessor(Professor p) {
        if (p == null) {
            return null;
        }
        return new CatalogProfessorDto(p.getId(), p.getFirstName(), p.getLastName());
    }

    private CatalogBookDto toCatalogBook(Book b) {
        return new CatalogBookDto(b.getId(), b.getTitle(), b.getPublicationDate(), toCatalogAuthor(b.getAuthor()));
    }

    private CatalogAuthorDto toCatalogAuthor(Author a) {
        if (a == null) {
            return null;
        }
        return new CatalogAuthorDto(a.getId(), a.getFirstName(), a.getLastName());
    }
}
