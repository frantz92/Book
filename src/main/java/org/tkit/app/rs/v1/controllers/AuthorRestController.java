package org.tkit.app.rs.v1.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.tkit.app.domain.daos.AuthorDAO;
import org.tkit.app.domain.models.criteria.AuthorSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.rs.v1.mappers.AuthorMapper;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.criteria.AuthorSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.exceptions.RestException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/author")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Authors REST")
public class AuthorRestController {

    @Inject
    AuthorDAO authorDAO;

    @Inject
    AuthorMapper authorMapper;

    @GET
    @Path("/{id}")
    @Operation(operationId = "getAuthorById", description = "Gets Author by ID")
    public Response getAuthorById(@PathParam("id") Long id) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            return Response.status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(authorMapper.mapToDTO(author))
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, "Author not found.");
    }

    @GET
    @Operation(operationId = "getAuthorByCriteria", description = "Gets Author by Criteria")
    public Response getAuthorByCriteria(@BeanParam AuthorSearchCriteriaDTO authorSearchCriteriaDTO) {

        AuthorSearchCriteria authorSearchCriteria = authorMapper.mapToSearchCriteria(authorSearchCriteriaDTO);

        PageResult<Author> authors = authorDAO.searchByCriteria(authorSearchCriteria);

        return Response.ok(authorMapper.mapToPageResultDTO(authors))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createAuthor", description = "Adds new Author")
    public Response saveAuthor(@Valid AuthorDTO authorDTO) {

        Author author = authorMapper.mapToEntity(authorDTO);

        return Response.status(Response.Status.CREATED)
                .entity(authorMapper.mapToDTO(authorDAO.create(author)))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateAuthor", description = "Update Author")
    public Response updateAuthor(@PathParam("id") Long id, @Valid AuthorDTO authorDTO) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            authorMapper.updateAuthorFromDto(authorDTO, author);
            return Response.status(Response.Status.CREATED)
                    .entity(authorMapper.mapToDTO(authorDAO.update(author)))
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, "Author not found.");
    }

    @DELETE
    @Path("/{id}")
    @Operation(operationId = "deleteAuthor", description = "Delete Author")
    public Response deleteAuthor(@PathParam("id") Long id) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            authorDAO.delete(author);
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, "Author not found.");
    }
}
