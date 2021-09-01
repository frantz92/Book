package org.tkit.app.rs.v1.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.models.criteria.BookSearchCriteriaDTO;
import org.tkit.app.rs.v1.services.BookServiceImpl;

@ApplicationScoped
@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Books REST")
public class BookRestController {

    @Inject
    BookServiceImpl bookServiceImpl;

    @GET
    @Path("/{bookIsbn}}")
    @Operation(operationId = "getBookByIsbn", description = "Gets book by ISBN")
    public Response getBookById(@PathParam("bookIsbn") Long bookIsbn) {

        return Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(bookServiceImpl.getBookById(bookIsbn))
                .build();
    }

    @GET
    @Operation(operationId = "getBookByCriteria", description = "Gets book by criteria")
    public Response getBookByCriteria(@BeanParam BookSearchCriteriaDTO bookSearchCriteriaDTO) {

        return Response.ok(bookServiceImpl.getBookByCriteria(bookSearchCriteriaDTO))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createBook", description = "Adds new book")
    public Response createBook(BookDTO bookDTO) {

        return Response.status(Response.Status.CREATED)
                .entity(bookServiceImpl.createBook(bookDTO))
                .build();
    }

    @PUT
    @Path("/{bookIsbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateBook", description = "Updates book info")
    public Response updateBook(@PathParam("bookIsbn") Long bookIsbn, @Valid BookDTO bookDTO) {

        return Response.status(Response.Status.CREATED)
                .entity(bookServiceImpl.updateBook(bookIsbn, bookDTO))
                .build();
    }

    @DELETE
    @Path("/{bookIsbn}")
    @Operation(operationId = "deleteBook", description = "Removes book")
    public Response deleteBook(@PathParam("bookIsbn") Long bookIsbn) {

        return bookServiceImpl.deleteBook(bookIsbn);
    }
}
