package org.example.spring.hateoas;

import org.example.spring.controllers.UserController;
import org.example.spring.dto.UserDto;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public @NonNull EntityModel<UserDto> toModel(UserDto dto) {
        Link selfLink = linkTo(methodOn(UserController.class).findById(dto.id())).withSelfRel();
        Link createLink = linkTo(methodOn(UserController.class).createUser(null)).withRel("create");
        Link updateLink = linkTo(methodOn(UserController.class).updateUser(dto.id(), null)).withRel("update");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteById(dto.id())).withRel("delete");
        Link allUsersLink = linkTo(methodOn(UserController.class).findAll()).withRel("all-users");
        Link emailLink = linkTo(methodOn(UserController.class).findByEmail(dto.email())).withRel("find-by-email");
        Link nameLink = linkTo(methodOn(UserController.class).findByName(dto.name())).withRel("find-by-name");
        Link ageLink = linkTo(methodOn(UserController.class).findByAge(dto.age())).withRel("find-by-age");

        return EntityModel.of(dto, selfLink, createLink, updateLink, deleteLink, allUsersLink, emailLink, nameLink, ageLink);
    }

}
