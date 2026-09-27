package org.example.spring.controllers;

import org.example.kafka.EmailDto;
import org.example.spring.services.NotificationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {
    private static final String REQUEST_MAPPING = "/api/notification";
    private static ObjectMapper mapper;
    private static EmailDto emailDto;

    @MockitoBean
    private NotificationService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        emailDto = new EmailDto("test@mail.ru");
    }

    @Test
    void when_sendUserCreatedToEmail_then_verify() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserCreatedToEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(emailDto)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service).sendUserCreatedToEmail(emailDto);
    }

    @Test
    void when_sendUserCreatedToEmailWithoutEmailDto_then_throwHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserCreatedToEmail"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpMessageNotReadableException.class));
    }

    @Test
    void when_sendUserCreatedToEmailWithNullEmailDto_then_verify() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserCreatedToEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpMessageNotReadableException.class));
    }

    @Test
    void when_sendUserDeletedToEmail_then_verify() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserDeletedToEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(emailDto)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service).sendUserDeletedToEmail(emailDto);
    }

    @Test
    void when_sendUserDeletedToEmailWithoutEmailDto_then_throwHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserDeletedToEmail"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpMessageNotReadableException.class));
    }

    @Test
    void when_sendUserDeletedToEmailWithNullEmailDto_then_verify() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserDeletedToEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpMessageNotReadableException.class));
    }

    @Test
    void when_nonexistentPath_then_returnNotFound() throws Exception {
        mockMvc.perform(get("/api/nonexistent/path"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(NoResourceFoundException.class));
    }

    @Test
    void when_throwSomeKindException_then_returnInternalServerError() throws Exception {
        doThrow(new RuntimeException("Some kind of excaption")).when(service).sendUserCreatedToEmail(emailDto);
        mockMvc.perform(post(REQUEST_MAPPING + "/sendUserCreatedToEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(emailDto)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(RuntimeException.class));
    }
}
