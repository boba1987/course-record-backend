package com.example.courserecord.service;

import com.example.courserecord.dto.StudentDto;
import com.example.courserecord.dto.StudentPayload;
import com.example.courserecord.entity.Student;
import com.example.courserecord.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public Page<StudentDto> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public StudentDto findById(Long id) {
        return toDto(studentRepository.findById(id).orElseThrow(() -> notFound("Student")));
    }

    public StudentDto create(StudentPayload payload) {
        Student s = new Student();
        s.setIndexNumber(payload.indexNumber());
        s.setFirstName(payload.firstName());
        s.setLastName(payload.lastName());
        return toDto(studentRepository.save(s));
    }

    public StudentDto update(Long id, StudentPayload payload) {
        Student s = studentRepository.findById(id).orElseThrow(() -> notFound("Student"));
        s.setIndexNumber(payload.indexNumber());
        s.setFirstName(payload.firstName());
        s.setLastName(payload.lastName());
        return toDto(studentRepository.save(s));
    }

    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw notFound("Student");
        }
        studentRepository.deleteById(id);
    }

    private StudentDto toDto(Student s) {
        return new StudentDto(s.getId(), s.getIndexNumber(), s.getFirstName(), s.getLastName());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
