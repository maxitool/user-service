package org.example.spring.hateoas.assemblers;

import org.example.spring.controllers.NotificationController;
import org.example.spring.dto.ApiRootDto;
import org.example.spring.hateoas.rels.NotificationRel;
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
        Link sendCreated = linkTo(methodOn(NotificationController.class).sendUserCreatedToEmail(null))
                .withRel(NotificationRel.SEND_USER_CREATED.getValue());
        Link sendDeleted = linkTo(methodOn(NotificationController.class).sendUserDeletedToEmail(null))
                .withRel(NotificationRel.SEND_USER_DELETED.getValue());

        return EntityModel.of(dto, sendCreated, sendDeleted);
    }
}
