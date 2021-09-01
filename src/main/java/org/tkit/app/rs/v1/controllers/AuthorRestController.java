package org.tkit.app.rs.v1.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.criteria.AuthorSearchCriteriaDTO;
import org.tkit.app.rs.v1.services.AuthorServiceImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/author")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Authors REST")
public class AuthorRestController {

    @Inject
    AuthorServiceImpl authorServiceImpl;

    @GET
    @Path("/{id}")
    @Operation(operationId = "getAuthorById", description = "Gets Author by ID")
    public Response getAuthorById(@PathParam("id") Long id) {

        return Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(authorServiceImpl.getAuthorById(id))
                .build();
    }

    @GET
    @Operation(operationId = "getAuthorByCriteria", description = "Gets Author by Criteria")
    public Response getAuthorByCriteria(@BeanParam AuthorSearchCriteriaDTO authorSearchCriteriaDTO) {

        return Response.ok(authorServiceImpl.getAuthorByCriteria(authorSearchCriteriaDTO))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createAuthor", description = "Adds new Author")
    public Response createAuthor(@Valid AuthorDTO authorDTO) {

        return Response.status(Response.Status.CREATED)
                .entity(authorServiceImpl.createAuthor(authorDTO))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateAuthor", description = "Update Author")
    public Response updateAuthor(@PathParam("id") Long id, @Valid AuthorDTO authorDTO) {

        return Response.status(Response.Status.CREATED)
                .entity(authorServiceImpl.updateAuthor(id, authorDTO))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(operationId = "deleteAuthor", description = "Delete Author")
    public Response deleteAuthor(@PathParam("id") Long id) {

        return authorServiceImpl.deleteAuthor(id);
    }
}
