package com.example.courserecord.bootstrap;

import com.example.courserecord.config.AppAdminProperties;
import com.example.courserecord.entity.AppUser;
import com.example.courserecord.entity.Course;
import com.example.courserecord.entity.CourseSemester;
import com.example.courserecord.entity.Professor;
import com.example.courserecord.repository.AppUserRepository;
import com.example.courserecord.repository.CourseRepository;
import com.example.courserecord.repository.ProfessorRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DataSeeder implements ApplicationRunner {

    private final ProfessorRepository professorRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppAdminProperties appAdminProperties;
    private final CourseRepository courseRepository;

    public DataSeeder(
            ProfessorRepository professorRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AppAdminProperties appAdminProperties,
            CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.appAdminProperties = appAdminProperties;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (professorRepository.count() > 0) {
            return;
        }

        AppUser admin = new AppUser();
        admin.setUsername(appAdminProperties.getUsername());
        admin.setPasswordHash(passwordEncoder.encode(appAdminProperties.getPassword()));
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        appUserRepository.save(admin);

        Professor savic = new Professor();
        savic.setFirstName("Dusan");
        savic.setLastName("Savic");
        savic = professorRepository.save(savic);

        Professor milic = new Professor();
        milic.setFirstName("Milos");
        milic.setLastName("Milic");
        professorRepository.save(milic);

        Course course = new Course();
        course.setCode("NST");
        course.setTitle("Napredne softverske tehnologije");
        course.setEspb(6);
        course.setProfessor(savic);

        CourseSemester semester = new CourseSemester();
        semester.setCourse(course);
        semester.setSemester(1);
        course.getSemesters().add(semester);

        courseRepository.save(course);
    }
}
