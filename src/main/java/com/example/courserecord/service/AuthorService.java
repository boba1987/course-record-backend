package com.example.courserecord.service;

import com.example.courserecord.dto.AuthorDto;
import com.example.courserecord.dto.AuthorPayload;
import com.example.courserecord.entity.Author;
import com.example.courserecord.repository.AuthorRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AuthorDto findById(Long id) {
        return toDto(authorRepository.findById(id).orElseThrow(() -> notFound("Author")));
    }

    public AuthorDto create(AuthorPayload payload) {
        Author a = new Author();
        a.setFirstName(payload.firstName());
        a.setLastName(payload.lastName());
        return toDto(authorRepository.save(a));
    }

    public AuthorDto update(Long id, AuthorPayload payload) {
        Author a = authorRepository.findById(id).orElseThrow(() -> notFound("Author"));
        a.setFirstName(payload.firstName());
        a.setLastName(payload.lastName());
        return toDto(authorRepository.save(a));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw notFound("Author");
        }
        authorRepository.deleteById(id);
    }

    private AuthorDto toDto(Author a) {
        return new AuthorDto(a.getId(), a.getFirstName(), a.getLastName());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
