package com.example.courserecord.repository;

import com.example.courserecord.entity.CourseBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseBookRepository extends JpaRepository<CourseBook, Long>, JpaSpecificationExecutor<CourseBook> {}
