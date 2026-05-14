package com.example.courserecord.service;

import com.example.courserecord.dto.CourseDto;
import com.example.courserecord.dto.CoursePayload;
import com.example.courserecord.dto.CourseSemesterDto;
import com.example.courserecord.dto.CourseSemesterPayload;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.CourseSemester;
import com.example.courserecord.entity.Professor;
import com.example.courserecord.repository.CourseRepository;
import com.example.courserecord.repository.ProfessorRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;

    public CourseService(CourseRepository courseRepository, ProfessorRepository professorRepository) {
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CourseDto findById(Long id) {
        Course c = courseRepository.findById(id).orElseThrow(() -> notFound("Course"));
        c.getSemesters().size();
        return toDto(c);
    }

    public CourseDto create(CoursePayload payload) {
        if (courseRepository.existsByCode(payload.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course code already exists");
        }
        Course c = new Course();
        c.setCode(payload.code());
        c.setTitle(payload.title());
        c.setEspb(payload.espb());
        applyProfessor(c, payload.professorId());
        applySemesters(c, payload.semesters());
        return toDto(courseRepository.save(c));
    }

    public CourseDto update(Long id, CoursePayload payload) {
        Course c = courseRepository.findById(id).orElseThrow(() -> notFound("Course"));
        if (!c.getCode().equals(payload.code()) && courseRepository.existsByCode(payload.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course code already exists");
        }
        c.setCode(payload.code());
        c.setTitle(payload.title());
        c.setEspb(payload.espb());
        applyProfessor(c, payload.professorId());
        if (payload.semesters() != null) {
            c.getSemesters().clear();
            for (CourseSemesterPayload s : payload.semesters()) {
                CourseSemester cs = new CourseSemester();
                cs.setCourse(c);
                cs.setAcademicYear(s.academicYear());
                cs.setSemester(s.semester());
                c.getSemesters().add(cs);
            }
        }
        return toDto(courseRepository.save(c));
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw notFound("Course");
        }
        courseRepository.deleteById(id);
    }

    private void applyProfessor(Course c, Long professorId) {
        if (professorId == null) {
            c.setProfessor(null);
            return;
        }
        Professor p = professorRepository.findById(professorId).orElseThrow(() -> notFound("Professor"));
        c.setProfessor(p);
    }

    private void applySemesters(Course c, List<CourseSemesterPayload> semesters) {
        if (semesters == null) {
            return;
        }
        c.getSemesters().clear();
        for (CourseSemesterPayload s : semesters) {
            CourseSemester cs = new CourseSemester();
            cs.setCourse(c);
            cs.setAcademicYear(s.academicYear());
            cs.setSemester(s.semester());
            c.getSemesters().add(cs);
        }
    }

    private CourseDto toDto(Course c) {
        List<CourseSemesterDto> sems =
                c.getSemesters().stream()
                        .sorted(
                                Comparator.comparingInt(CourseSemester::getAcademicYear)
                                        .thenComparingInt(CourseSemester::getSemester))
                        .map(
                                s ->
                                        new CourseSemesterDto(
                                                s.getId(), c.getId(), s.getAcademicYear(), s.getSemester()))
                        .toList();
        Long profId = c.getProfessor() == null ? null : c.getProfessor().getId();
        return new CourseDto(c.getId(), c.getCode(), c.getTitle(), c.getEspb(), profId, sems);
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
