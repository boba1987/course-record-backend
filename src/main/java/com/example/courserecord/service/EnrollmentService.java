package com.example.courserecord.service;

import com.example.courserecord.dto.EnrollmentDto;
import com.example.courserecord.dto.EnrollmentPayload;
import com.example.courserecord.entity.Course;
import com.example.courserecord.jpa.EnrollmentSpecifications;
import com.example.courserecord.entity.Enrollment;
import com.example.courserecord.entity.Student;
import com.example.courserecord.repository.CourseRepository;
import com.example.courserecord.repository.EnrollmentRepository;
import com.example.courserecord.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentDto> findAll(Pageable pageable, String student, String course) {
        if (!StringUtils.hasText(student) && !StringUtils.hasText(course)) {
            return enrollmentRepository.findAll(pageable).map(this::toDto);
        }
        return enrollmentRepository
                .findAll(EnrollmentSpecifications.withOptionalFilters(student, course), pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public EnrollmentDto findById(Long id) {
        return toDto(enrollmentRepository.findById(id).orElseThrow(() -> notFound("Enrollment")));
    }

    public EnrollmentDto create(EnrollmentPayload payload) {
        Student student = studentRepository.findById(payload.studentId()).orElseThrow(() -> notFound("Student"));
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        return toDto(enrollmentRepository.save(e));
    }

    public EnrollmentDto update(Long id, EnrollmentPayload payload) {
        Enrollment e = enrollmentRepository.findById(id).orElseThrow(() -> notFound("Enrollment"));
        Student student = studentRepository.findById(payload.studentId()).orElseThrow(() -> notFound("Student"));
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        e.setStudent(student);
        e.setCourse(course);
        return toDto(enrollmentRepository.save(e));
    }

    public void delete(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw notFound("Enrollment");
        }
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentDto toDto(Enrollment e) {
        return new EnrollmentDto(e.getId(), e.getStudent().getId(), e.getCourse().getId());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
