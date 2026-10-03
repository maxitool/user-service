package org.example.spring.hateoas.assemblers;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.controllers.UserController;
import org.example.spring.dto.UserDto;
import org.example.spring.hateoas.rels.UserRel;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@Slf4j
public class UserAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public @NonNull EntityModel<UserDto> toModel(UserDto dto) {
        Link selfLink = linkTo(methodOn(UserController.class).findById(dto.id())).withSelfRel();
        Link updateLink = linkTo(methodOn(UserController.class).updateUser(dto.id(), null))
                .withRel(UserRel.UPDATE.getValue());
        Link deleteLink = linkTo(methodOn(UserController.class).deleteById(dto.id()))
                .withRel(UserRel.DELETE.getValue());
        Link nameLink = linkTo(methodOn(UserController.class).findByName(dto.name()))
                .withRel(UserRel.FIND_USER_BY_NAME.getValue());
        Link ageLink = linkTo(methodOn(UserController.class).findByAge(dto.age()))
                .withRel(UserRel.FIND_USER_BY_AGE.getValue());
        log.debug("Links in toModel method created.");

        return EntityModel.of(dto, selfLink, updateLink, deleteLink, nameLink, ageLink);
    }

    @Override
    public @NonNull CollectionModel<EntityModel<UserDto>> toCollectionModel(
            @NonNull Iterable<? extends UserDto> entities) {
        List<EntityModel<UserDto>> models = new ArrayList<>();
        entities.forEach(dto -> {
            EntityModel<UserDto> model = toModel(dto);
            log.debug("dto {} to model {} completed:", dto, model);
            models.add(model);
        });

        Link findAll = linkTo(methodOn(UserController.class).findAll())
                .withRel(UserRel.GET_ALL_USERS.getValue());
        Link createLink = linkTo(methodOn(UserController.class).createUser(null))
                .withRel(UserRel.CREATE.getValue());
        Link emailLink = linkTo(methodOn(UserController.class).findByEmail(null))
                .withRel(UserRel.FIND_USER_BY_EMAIL.getValue());
        Link nameLink = linkTo(methodOn(UserController.class).findByName(null))
                .withRel(UserRel.FIND_USER_BY_NAME.getValue());
        Link ageLink = linkTo(methodOn(UserController.class).findByAge(null))
                .withRel(UserRel.FIND_USER_BY_AGE.getValue());
        log.debug("Links in toCollectionModel method created.");

        return CollectionModel.of(models, findAll, createLink,
                emailLink, nameLink, ageLink);
    }
}
