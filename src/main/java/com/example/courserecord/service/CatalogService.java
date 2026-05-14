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

    public List<CatalogCourseDto> listCourses() {
        return courseRepository.findAllForPublicCatalog().stream().map(this::toCatalogCourse).toList();
    }

    public List<CatalogBookDto> listBooks() {
        return bookRepository.findAllWithAuthor().stream().map(this::toCatalogBook).toList();
    }

    public List<CatalogAuthorDto> listAuthors() {
        return authorRepository.findAll().stream().map(this::toCatalogAuthor).toList();
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
