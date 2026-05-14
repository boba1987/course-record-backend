package com.example.courserecord.repository;

import com.example.courserecord.entity.CourseBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseBookRepository extends JpaRepository<CourseBook, Long> {}
