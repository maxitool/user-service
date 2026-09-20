package org.example.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;
import org.example.spring.exception.ErrorMessag;
import org.example.spring.exception.UserAlreadyExistsException;
import org.example.spring.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String REQUEST_MAPPING = "/api/users";
    private static ObjectMapper mapper;
    private static UserDto userIrina1;
    private static UserDto userIrina2;
    private static List<UserDto> users;
    private static UserCreateUpdateDto userIrina1CreateUpdateDto;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        userIrina1 = new UserDto(1L, "Irina", "test1@mail.ru", 13);
        userIrina1CreateUpdateDto = new UserCreateUpdateDto(userIrina1.name(), userIrina1.email(), userIrina1.age());
        userIrina2 = new UserDto(2L, userIrina1.name(), "test2@mail.ru", userIrina1.age());
        users = List.of(userIrina1, userIrina2);
    }

    @Test
    void when_findAll_then_returnUsersDtoListAndVerify() throws Exception {
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get(REQUEST_MAPPING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(users.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(users.get(0).name()))
                .andExpect(jsonPath("$[0].email").value(users.get(0).email()))
                .andExpect(jsonPath("$[0].age").value(users.get(0).age()))
                .andExpect(jsonPath("$[1].id").value(users.get(1).id()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).name()))
                .andExpect(jsonPath("$[1].email").value(users.get(1).email()))
                .andExpect(jsonPath("$[1].age").value(users.get(1).age()));
        verify(userService).findAll();
    }

    @Test
    void when_findByExistentId_then_returnUserDtoAndVerify() throws Exception {
        when(userService.findById(userIrina1.id())).thenReturn(userIrina1);

        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", userIrina1.id()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).findById(any(Long.class));
    }

    @Test
    void when_findByNonExistentId_then_returnNotFoundAndVerify() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException(
                String.format(ErrorMessag.USER_NOT_FOND_ID, 9999L));
        when(userService.findById(9999L)).thenThrow(exception);

        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", 9999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class));
        verify(userService).findById(any(Long.class));
    }

    @Test
    void when_findByExistentEmail_then_returnUserDtoAndVerify() throws Exception {
        when(userService.findByEmail(userIrina1.email())).thenReturn(userIrina1);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByEmail")
                        .param("email", userIrina1.email()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).findByEmail(any(String.class));
    }

    @Test
    void when_findByNonExistentEmail_then_returnNotFoundAndVerify() throws Exception {
        String email = "Non" + userIrina1.email();
        EntityNotFoundException exception = new EntityNotFoundException(
                String.format(ErrorMessag.USER_NOT_FOND_EMAIL, email));
        when(userService.findByEmail(email)).thenThrow(exception);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByEmail")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class));
        verify(userService).findByEmail(any(String.class));
    }

    @Test
    void when_findByEmailWithoutParam_then_returnNotFound() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + "/findByEmail"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MissingServletRequestParameterException.class));
    }

    @Test
    void when_findByName_then_returnUsersDtoListAndVerify() throws Exception {
        when(userService.findByName(userIrina1.name())).thenReturn(users);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByName")
                        .param("name", userIrina1.name()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(users.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(users.get(0).name()))
                .andExpect(jsonPath("$[0].email").value(users.get(0).email()))
                .andExpect(jsonPath("$[0].age").value(users.get(0).age()))
                .andExpect(jsonPath("$[1].id").value(users.get(1).id()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).name()))
                .andExpect(jsonPath("$[1].email").value(users.get(1).email()))
                .andExpect(jsonPath("$[1].age").value(users.get(1).age()));
        verify(userService).findByName(any(String.class));
    }

    @Test
    void when_findByNameWithoutParam_then_returnNotFound() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + "/findByName"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MissingServletRequestParameterException.class));
    }

    @Test
    void when_findByAge_then_returnUsersDtoListAndVerify() throws Exception {
        when(userService.findByAge(userIrina1.age())).thenReturn(users);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByAge")
                        .param("age", userIrina1.age().toString()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(users.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(users.get(0).name()))
                .andExpect(jsonPath("$[0].email").value(users.get(0).email()))
                .andExpect(jsonPath("$[0].age").value(users.get(0).age()))
                .andExpect(jsonPath("$[1].id").value(users.get(1).id()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).name()))
                .andExpect(jsonPath("$[1].email").value(users.get(1).email()))
                .andExpect(jsonPath("$[1].age").value(users.get(1).age()));
        verify(userService).findByAge(any(Integer.class));
    }

    @Test
    void when_findByAgeWithoutParam_then_returnNotFound() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + "/findByAge"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MissingServletRequestParameterException.class));
    }

    @Test
    void when_createUser_then_returnUserDtoAndVerify() throws Exception {
        when(userService.createUser(userIrina1CreateUpdateDto)).thenReturn(userIrina1);

        mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIrina1CreateUpdateDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).createUser(any(UserCreateUpdateDto.class));
    }

    @Test
    void when_createUserWithNegativeAge_then_returnBadRequest() throws Exception {
        UserCreateUpdateDto invalidDto = UserCreateUpdateDto.builder()
                .name("Irina")
                .email("test1@mail.ru")
                .age(-1)
                .build();

        mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    void when_createUserWithInvalidEmail_then_returnBadRequest() throws Exception {
        UserCreateUpdateDto invalidDto = UserCreateUpdateDto.builder()
                .name("Irina")
                .email("badEmail")
                .age(1)
                .build();

        mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    void when_createUserWithDuplicateEmail_then_returnConflictAndVerify() throws Exception {
        UserAlreadyExistsException exception = new UserAlreadyExistsException(userIrina1CreateUpdateDto.email());
        when(userService.createUser(userIrina1CreateUpdateDto)).thenThrow(exception);

        mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIrina1CreateUpdateDto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(UserAlreadyExistsException.class));
        verify(userService).createUser(any(UserCreateUpdateDto.class));
    }

    @Test
    void when_updateUser_then_returnUserDtoAndVerify() throws Exception {
        when(userService.updateUser(userIrina1.id(), userIrina1CreateUpdateDto)).thenReturn(userIrina1);

        mockMvc.perform(put(REQUEST_MAPPING + "/{id}", userIrina1.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIrina1CreateUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).updateUser(any(Long.class), any(UserCreateUpdateDto.class));
    }

    @Test
    void when_updateUserWithNonExistentId_then_returnNotFoundAndVerify() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException(
                String.format(ErrorMessag.USER_NOT_FOND_ID, 9999L));
        when(userService.updateUser(9999L, userIrina1CreateUpdateDto)).thenThrow(exception);

        mockMvc.perform(put(REQUEST_MAPPING + "/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIrina1CreateUpdateDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class));
        verify(userService).updateUser(any(Long.class), any(UserCreateUpdateDto.class));
    }

    @Test
    void when_deleteById_then_verify() throws Exception {
        mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", userIrina1.id()))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(userService).deleteById(any(Long.class));
    }

    @Test
    void when_deleteByNonExistentId_then_returnNotFoundAndVerify() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException(
                String.format(ErrorMessag.USER_NOT_FOND_ID, 9999L));
        doThrow(exception).when(userService).deleteById(9999L);

        mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", 9999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class));
        verify(userService).deleteById(any(Long.class));
    }

    @Test
    void when_createUserWithBadJson_then_returnBadRequest() throws Exception {
        String malformedJson = "{\"name\":\"Irina\", \"email\":\"test1@mail.ru\", \"age\":\"not-a-number\"}";

        mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpMessageNotReadableException.class));
    }

    @Test
    void when_findByIdWithBadAgrInUrl_then_returnBadRequest() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", "notId"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentTypeMismatchException.class));
    }

    @Test
    void when_postToGetOnlyEndpoint_then_returnMethodNotAllowed() throws Exception {
        mockMvc.perform(post(REQUEST_MAPPING + "/findByAge"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(HttpRequestMethodNotSupportedException.class));
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
        when(userService.findById(userIrina1.id())).thenThrow(new RuntimeException("Some kind of excaption"));

        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", userIrina1.id()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(RuntimeException.class));
    }
}
