package com.example.courserecord.config;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

/**
 * Replaces Spring Boot’s {@code SpringDataWebAutoConfiguration} so we can set {@link PageSerializationMode#VIA_DTO}: every
 * {@link org.springframework.data.domain.Page} is serialized as {@link org.springframework.data.web.PagedModel}
 * (stable JSON: {@code content} + nested {@code page} metadata). Same pageable/sort tuning as Boot’s auto-config.
 *
 * @see com.example.courserecord.CourseRecordApplication
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpringDataWebProperties.class)
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class SpringDataWebConfiguration {

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer(SpringDataWebProperties properties) {
        return resolver -> {
            SpringDataWebProperties.Pageable pageable = properties.getPageable();
            resolver.setPageParameterName(pageable.getPageParameter());
            resolver.setSizeParameterName(pageable.getSizeParameter());
            resolver.setOneIndexedParameters(pageable.isOneIndexedParameters());
            resolver.setPrefix(pageable.getPrefix());
            resolver.setQualifierDelimiter(pageable.getQualifierDelimiter());
            resolver.setFallbackPageable(PageRequest.of(0, pageable.getDefaultPageSize()));
            resolver.setMaxPageSize(pageable.getMaxPageSize());
        };
    }

    @Bean
    SortHandlerMethodArgumentResolverCustomizer sortCustomizer(SpringDataWebProperties properties) {
        return resolver -> resolver.setSortParameter(properties.getSort().getSortParameter());
    }
}
