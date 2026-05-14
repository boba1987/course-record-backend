package com.example.courserecord.repository;

import com.example.courserecord.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {}
