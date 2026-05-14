package com.example.courserecord.service;

import com.example.courserecord.dto.CourseBookDto;
import com.example.courserecord.dto.CourseBookPayload;
import com.example.courserecord.entity.Book;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.CourseBook;
import com.example.courserecord.jpa.CourseBookSpecifications;
import com.example.courserecord.repository.BookRepository;
import com.example.courserecord.repository.CourseBookRepository;
import com.example.courserecord.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CourseBookService {

    private final CourseBookRepository courseBookRepository;
    private final CourseRepository courseRepository;
    private final BookRepository bookRepository;

    public CourseBookService(
            CourseBookRepository courseBookRepository,
            CourseRepository courseRepository,
            BookRepository bookRepository) {
        this.courseBookRepository = courseBookRepository;
        this.courseRepository = courseRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<CourseBookDto> findAll(Pageable pageable, String courseName, String bookName) {
        if (!StringUtils.hasText(courseName) && !StringUtils.hasText(bookName)) {
            return courseBookRepository.findAll(pageable).map(this::toDto);
        }
        return courseBookRepository
                .findAll(CourseBookSpecifications.withOptionalFilters(courseName, bookName), pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public CourseBookDto findById(Long id) {
        return toDto(courseBookRepository.findById(id).orElseThrow(() -> notFound("Course book")));
    }

    public CourseBookDto create(CourseBookPayload payload) {
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        Book book = bookRepository.findById(payload.bookId()).orElseThrow(() -> notFound("Book"));
        CourseBook cb = new CourseBook();
        cb.setCourse(course);
        cb.setBook(book);
        course.getCourseBooks().add(cb);
        courseRepository.save(course);
        return toDto(cb);
    }

    public CourseBookDto update(Long id, CourseBookPayload payload) {
        CourseBook cb = courseBookRepository.findById(id).orElseThrow(() -> notFound("Course book"));
        Course newCourse = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        Book newBook = bookRepository.findById(payload.bookId()).orElseThrow(() -> notFound("Book"));
        Course oldCourse = cb.getCourse();
        if (!oldCourse.getId().equals(newCourse.getId())) {
            oldCourse.getCourseBooks().remove(cb);
            courseRepository.save(oldCourse);
            cb.setCourse(newCourse);
            newCourse.getCourseBooks().add(cb);
        }
        cb.setBook(newBook);
        courseRepository.save(newCourse);
        return toDto(courseBookRepository.save(cb));
    }

    public void delete(Long id) {
        CourseBook cb = courseBookRepository.findById(id).orElseThrow(() -> notFound("Course book"));
        Course course = cb.getCourse();
        course.getCourseBooks().remove(cb);
        courseRepository.save(course);
    }

    private CourseBookDto toDto(CourseBook cb) {
        return new CourseBookDto(cb.getId(), cb.getCourse().getId(), cb.getBook().getId());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
