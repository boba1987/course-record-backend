package com.example.courserecord.service;

import com.example.courserecord.dto.BookDto;
import com.example.courserecord.dto.BookPayload;
import com.example.courserecord.entity.Author;
import com.example.courserecord.entity.Book;
import com.example.courserecord.repository.AuthorRepository;
import com.example.courserecord.repository.BookRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BookDto findById(Long id) {
        return toDto(bookRepository.findById(id).orElseThrow(() -> notFound("Book")));
    }

    public BookDto create(BookPayload payload) {
        Book b = new Book();
        b.setTitle(payload.title());
        b.setPublicationDate(payload.publicationDate());
        applyAuthor(b, payload.authorId());
        return toDto(bookRepository.save(b));
    }

    public BookDto update(Long id, BookPayload payload) {
        Book b = bookRepository.findById(id).orElseThrow(() -> notFound("Book"));
        b.setTitle(payload.title());
        b.setPublicationDate(payload.publicationDate());
        applyAuthor(b, payload.authorId());
        return toDto(bookRepository.save(b));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw notFound("Book");
        }
        bookRepository.deleteById(id);
    }

    private void applyAuthor(Book b, Long authorId) {
        if (authorId == null) {
            b.setAuthor(null);
            return;
        }
        Author a = authorRepository.findById(authorId).orElseThrow(() -> notFound("Author"));
        b.setAuthor(a);
    }

    private BookDto toDto(Book b) {
        Long authorId = b.getAuthor() == null ? null : b.getAuthor().getId();
        return new BookDto(b.getId(), b.getTitle(), b.getPublicationDate(), authorId);
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
