package org.example.spring.hateoas.assemblers;

import org.example.spring.controllers.UserController;
import org.example.spring.dto.ApiRootDto;
import org.example.spring.hateoas.rels.UserRel;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RepresentationAssembler
        implements RepresentationModelAssembler<ApiRootDto, EntityModel<ApiRootDto>> {

    @Override
    public @NonNull EntityModel<ApiRootDto> toModel(@NonNull ApiRootDto dto) {
        Link allUsersLink = linkTo(methodOn(UserController.class).findAll())
                .withRel(UserRel.GET_ALL_USERS.getValue());
        Link createLink = linkTo(methodOn(UserController.class).createUser(null))
                .withRel(UserRel.CREATE.getValue());
        Link idLink = linkTo(methodOn(UserController.class).findById(null))
                .withRel(UserRel.FIND_USER_BY_ID.getValue());
        Link updateLink = linkTo(methodOn(UserController.class).updateUser(null, null))
                .withRel(UserRel.UPDATE.getValue());
        Link deleteLink = linkTo(methodOn(UserController.class).deleteById(null))
                .withRel(UserRel.DELETE.getValue());
        Link emailLink = linkTo(methodOn(UserController.class).findByEmail(null))
                .withRel(UserRel.FIND_USER_BY_EMAIL.getValue());
        Link nameLink = linkTo(methodOn(UserController.class).findByName(null))
                .withRel(UserRel.FIND_USER_BY_NAME.getValue());
        Link ageLink = linkTo(methodOn(UserController.class).findByAge(null))
                .withRel(UserRel.FIND_USER_BY_AGE.getValue());

        return EntityModel.of(dto, allUsersLink, createLink, idLink,
                updateLink, deleteLink, emailLink, nameLink, ageLink);
    }
}
