package org.example.spring.controllers;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Users",
        description = "Operations for creating, reading, updating and deleting users"
)
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users successfully received",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = UserDto.class)
                    )
            )
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(
            summary = "Get user by id",
            description = "Returns a user with the specified identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully found",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Find user by email",
            description = "Returns a user with the specified email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully found",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Operation(
            summary = "Find users by name",
            description = "Returns users with the specified name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = UserDto.class)
                    )
            )
    )

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<UserDto>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @Operation(
            summary = "Find users by age",
            description = "Returns users with the specified age"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = UserDto.class)
                    )
            )
    )
    @GetMapping("/findByAge/{age}")
    public ResponseEntity<List<UserDto>> findByAge(@PathVariable Integer age) {
        return ResponseEntity.ok(userService.findByAge(age));
    }

    @Operation(
            summary = "Create user",
            description = "Creates a new user. Email must be unique"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request data is invalid"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this email already exists"
            )
    })

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateUpdateDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }
    @Operation(
            summary = "Update user",
            description = "Updates the user with the specified identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request data is invalid"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserCreateUpdateDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }
    @Operation(
            summary = "Delete user",
            description = "Deletes the user with the specified identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
