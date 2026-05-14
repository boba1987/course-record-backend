package com.example.courserecord;

import com.example.courserecord.config.AppAdminProperties;
import com.example.courserecord.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = SpringDataWebAutoConfiguration.class)
@EnableConfigurationProperties({JwtProperties.class, AppAdminProperties.class})
public class CourseRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseRecordApplication.class, args);
    }
}
