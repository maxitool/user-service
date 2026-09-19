package org.example.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;
import org.example.spring.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<UserDto>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @GetMapping("/findByAge/{age}")
    public ResponseEntity<List<UserDto>> findByAge(@PathVariable Integer age) {
        return ResponseEntity.ok(userService.findByAge(age));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(UserCreateUpdateDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, UserCreateUpdateDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
