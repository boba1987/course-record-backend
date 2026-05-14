package com.example.courserecord.service;

import com.example.courserecord.dto.ProfessorDto;
import com.example.courserecord.dto.ProfessorPayload;
import com.example.courserecord.entity.Professor;
import com.example.courserecord.jpa.ProfessorSpecifications;
import com.example.courserecord.repository.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProfessorDto> findAll(Pageable pageable, String firstName, String lastName) {
        if (!StringUtils.hasText(firstName) && !StringUtils.hasText(lastName)) {
            return professorRepository.findAll(pageable).map(this::toDto);
        }
        return professorRepository
                .findAll(ProfessorSpecifications.withOptionalNameFilters(firstName, lastName), pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ProfessorDto findById(Long id) {
        return toDto(professorRepository.findById(id).orElseThrow(() -> notFound("Professor")));
    }

    public ProfessorDto create(ProfessorPayload payload) {
        Professor p = new Professor();
        p.setFirstName(payload.firstName());
        p.setLastName(payload.lastName());
        return toDto(professorRepository.save(p));
    }

    public ProfessorDto update(Long id, ProfessorPayload payload) {
        Professor p = professorRepository.findById(id).orElseThrow(() -> notFound("Professor"));
        p.setFirstName(payload.firstName());
        p.setLastName(payload.lastName());
        return toDto(professorRepository.save(p));
    }

    public void delete(Long id) {
        if (!professorRepository.existsById(id)) {
            throw notFound("Professor");
        }
        professorRepository.deleteById(id);
    }

    private ProfessorDto toDto(Professor p) {
        return new ProfessorDto(p.getId(), p.getFirstName(), p.getLastName());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
