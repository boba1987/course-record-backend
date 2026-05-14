package com.example.courserecord;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.courserecord.dto.auth.LoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseRecordApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void publicCatalogEndpointsReturn200WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/courses")).andExpect(status().isOk());
        mockMvc.perform(get("/api/public/books")).andExpect(status().isOk());
        mockMvc.perform(get("/api/public/authors")).andExpect(status().isOk());
    }

    @Test
    void createProfessorWithoutTokenReturns401() throws Exception {
        mockMvc.perform(
                        post("/api/professors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\":\"X\",\"lastName\":\"Y\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginThenCreateProfessorReturns201() throws Exception {
        LoginRequest login = new LoginRequest("admin", "testpass");
        MvcResult result =
                mockMvc.perform(
                                post("/api/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(login)))
                        .andExpect(status().isOk())
                        .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String token = body.get("accessToken").asText();

        mockMvc.perform(
                        post("/api/professors")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\":\"New\",\"lastName\":\"Professor\"}"))
                .andExpect(status().isCreated());
    }
}
