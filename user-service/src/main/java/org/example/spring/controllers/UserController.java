package org.example.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spring.dto.ApiRootDto;
import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;
import org.example.spring.hateoas.assemblers.RepresentationAssembler;
import org.example.spring.services.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/api/v1/users",
        produces = {
                "application/prs.hal-forms+json",
                "application/hal+json",
                "application/json"
        }
)
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Users",
        description = "Operations for creating, reading, updating and deleting users"
)
public class UserController {
    private final UserService userService;
    private final RepresentationAssembler representationAssembler;

    @Operation(
            summary = "Get api",
            description = "Returns users api"
    )
    @ApiResponse(
            responseCode = "200",
            description = "api",
            content = @Content(
                    schema = @Schema(implementation = ApiRootDto.class)
            )
    )
    @GetMapping("/getApi")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<ApiRootDto> getApi() {
        return representationAssembler.toModel(new ApiRootDto());
    }

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
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<UserDto>> findAll() {
        return userService.findAll();
    }

    @Operation(
            summary = "Get user by id",
            description = "Returns a user with the specified identifier"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully found",
            content = @Content(
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDto> findById(
            @PathVariable
            @Parameter(name = "id", description = "User id", example = "1")
            Long id) {
        return userService.findById(id);
    }

    @Operation(
            summary = "Find user by email",
            description = "Returns a user with the specified email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully found",
            content = @Content(
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )

    @GetMapping("/findByEmail")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDto> findByEmail(
            @RequestParam
            @Parameter(name = "email", description = "User email", example = "test@mail.ru")
            String email) {
        return userService.findByEmail(email);
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
    @GetMapping("/findByName")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<UserDto>> findByName(
            @RequestParam
            @Parameter(name = "name", description = "User name", example = "Ivan Ivanov")
            String name) {
        return userService.findByName(name);
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
    @GetMapping("/findByAge")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<UserDto>> findByAge(
            @RequestParam
            @Parameter(name = "age", description = "User age", example = "12")
            Integer age) {
        return userService.findByAge(age);
    }

    @Operation(
            summary = "Create user",
            description = "Creates a new user. Email must be unique"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User successfully created",
            content = @Content(
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Request data is invalid"
    )
    @ApiResponse(
            responseCode = "409",
            description = "User with this email already exists"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDto> createUser(
            @RequestBody
            @Valid
            UserCreateUpdateDto userDto) {
        return userService.createUser(userDto);
    }

    @Operation(
            summary = "Update user",
            description = "Updates the user with the specified identifier"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully updated",
            content = @Content(
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Request data is invalid"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDto> updateUser(
            @PathVariable
            @Parameter(name = "id", description = "User id", example = "1")
            Long id,
            @RequestBody
            @Valid
            UserCreateUpdateDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes the user with the specified identifier"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User successfully deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @Parameter(name = "id", description = "User id", example = "1")
            Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
