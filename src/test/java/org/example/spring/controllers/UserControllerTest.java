package org.example.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;
import org.example.spring.entities.User;
import org.example.spring.exception.ErrorMessag;
import org.example.spring.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String REQUEST_MAPPING ="/api/users";
    private static ObjectMapper mapper;
    private static UserDto userIrina1;
    private static UserDto userIrina2;
    private static List<UserDto> users;
    private static UserCreateUpdateDto userIrina1CreateUpdateDto;
    private static EntityNotFoundException entityNotFoundException;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        userIrina1 = new UserDto(1L,"Irina", "test1@mail.ru", 13);
        userIrina1CreateUpdateDto = new UserCreateUpdateDto(userIrina1.name(), userIrina1.email(), userIrina1.age());
        userIrina2 = new UserDto(2L,userIrina1.name(), "test2@mail.ru", userIrina1.age());
        users = List.of(userIrina1, userIrina2);
        entityNotFoundException = new EntityNotFoundException(String.format(ErrorMessag.USER_NOT_FOND, 1L));
    }

    @Test
    void when_findAll_then_returnUsersDtoList() throws Exception {
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
    void when_findByExistentId_then_returnUserDto() throws Exception {
        when(userService.findById(userIrina1.id())).thenReturn(userIrina1);

        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", userIrina1.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).findById(any(Long.class));
    }

    @Test
    void when_findByNonExistentId_then_returnEntityNotFoundException() throws Exception {
        when(userService.findById(9999L)).thenThrow(entityNotFoundException);

        mockMvc.perform(get(REQUEST_MAPPING + "/{id}", 9999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()));
        verify(userService).findById(any(Long.class));
    }

    @Test
    void when_findByEmail_then_returnUserDto() throws Exception {
        when(userService.findByEmail(userIrina1.email())).thenReturn(userIrina1);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByEmail/{email}", userIrina1.email()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userIrina1.id()))
                .andExpect(jsonPath("$.name").value(userIrina1.name()))
                .andExpect(jsonPath("$.email").value(userIrina1.email()))
                .andExpect(jsonPath("$.age").value(userIrina1.age()));
        verify(userService).findByEmail(any(String.class));
    }

    @Test
    void when_findByName_then_returnUsersDtoList() throws Exception {
        when(userService.findByName(userIrina1.name())).thenReturn(users);

        mockMvc.perform(get(REQUEST_MAPPING + "/findByName/{name}", userIrina1.name()))
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
        verify(userService).findByName(any(String.class));
    }

    @Test
    void when_createUser_then_returnUserDto() throws Exception {
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
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
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
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }


//MethodArgumentNotValidException
    // add when_findByEmail_then_returnEx
}
