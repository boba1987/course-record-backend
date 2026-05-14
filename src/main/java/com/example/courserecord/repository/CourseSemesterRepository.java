package com.example.courserecord.repository;

import com.example.courserecord.entity.CourseSemester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSemesterRepository extends JpaRepository<CourseSemester, Long> {

    boolean existsByCourse_IdAndSemester(Long courseId, int semester);

    boolean existsByCourse_IdAndSemesterAndIdNot(Long courseId, int semester, Long id);
}
