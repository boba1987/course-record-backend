package com.example.courserecord.service;

import com.example.courserecord.dto.ExamDto;
import com.example.courserecord.dto.ExamPayload;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.Exam;
import com.example.courserecord.entity.Student;
import com.example.courserecord.repository.CourseRepository;
import com.example.courserecord.repository.ExamRepository;
import com.example.courserecord.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ExamService {

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ExamService(
            ExamRepository examRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Page<ExamDto> findAll(Pageable pageable) {
        return examRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ExamDto findById(Long id) {
        return toDto(examRepository.findById(id).orElseThrow(() -> notFound("Exam")));
    }

    public ExamDto create(ExamPayload payload) {
        Student student = studentRepository.findById(payload.studentId()).orElseThrow(() -> notFound("Student"));
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        Exam e = new Exam();
        e.setStudent(student);
        e.setCourse(course);
        e.setExamDate(payload.examDate());
        e.setGrade(payload.grade());
        return toDto(examRepository.save(e));
    }

    public ExamDto update(Long id, ExamPayload payload) {
        Exam e = examRepository.findById(id).orElseThrow(() -> notFound("Exam"));
        Student student = studentRepository.findById(payload.studentId()).orElseThrow(() -> notFound("Student"));
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        e.setStudent(student);
        e.setCourse(course);
        e.setExamDate(payload.examDate());
        e.setGrade(payload.grade());
        return toDto(examRepository.save(e));
    }

    public void delete(Long id) {
        if (!examRepository.existsById(id)) {
            throw notFound("Exam");
        }
        examRepository.deleteById(id);
    }

    private ExamDto toDto(Exam e) {
        return new ExamDto(
                e.getId(), e.getStudent().getId(), e.getCourse().getId(), e.getExamDate(), e.getGrade());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
