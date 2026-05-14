package com.example.courserecord.service;

import com.example.courserecord.dto.CourseSemesterDto;
import com.example.courserecord.dto.CourseSemesterUpsertPayload;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.CourseSemester;
import com.example.courserecord.repository.CourseRepository;
import com.example.courserecord.repository.CourseSemesterRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CourseSemesterService {

    private final CourseSemesterRepository courseSemesterRepository;
    private final CourseRepository courseRepository;

    public CourseSemesterService(
            CourseSemesterRepository courseSemesterRepository, CourseRepository courseRepository) {
        this.courseSemesterRepository = courseSemesterRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseSemesterDto> findAll() {
        return courseSemesterRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CourseSemesterDto findById(Long id) {
        return toDto(courseSemesterRepository.findById(id).orElseThrow(() -> notFound("Course semester")));
    }

    public CourseSemesterDto create(CourseSemesterUpsertPayload payload) {
        Course course = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        CourseSemester cs = new CourseSemester();
        cs.setCourse(course);
        cs.setAcademicYear(payload.academicYear());
        cs.setSemester(payload.semester());
        course.getSemesters().add(cs);
        courseRepository.save(course);
        return toDto(cs);
    }

    public CourseSemesterDto update(Long id, CourseSemesterUpsertPayload payload) {
        CourseSemester cs = courseSemesterRepository.findById(id).orElseThrow(() -> notFound("Course semester"));
        Course newCourse = courseRepository.findById(payload.courseId()).orElseThrow(() -> notFound("Course"));
        if (!cs.getCourse().getId().equals(newCourse.getId())) {
            cs.getCourse().getSemesters().remove(cs);
            cs.setCourse(newCourse);
            newCourse.getSemesters().add(cs);
            courseRepository.save(cs.getCourse());
        }
        cs.setAcademicYear(payload.academicYear());
        cs.setSemester(payload.semester());
        courseRepository.save(newCourse);
        return toDto(courseSemesterRepository.save(cs));
    }

    public void delete(Long id) {
        CourseSemester cs = courseSemesterRepository.findById(id).orElseThrow(() -> notFound("Course semester"));
        Course course = cs.getCourse();
        course.getSemesters().remove(cs);
        courseRepository.save(course);
    }

    private CourseSemesterDto toDto(CourseSemester s) {
        return new CourseSemesterDto(
                s.getId(), s.getCourse().getId(), s.getAcademicYear(), s.getSemester());
    }

    private static ResponseStatusException notFound(String entity) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found");
    }
}
