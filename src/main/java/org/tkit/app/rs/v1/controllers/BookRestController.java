package org.tkit.app.rs.v1.controllers;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.tkit.app.domain.daos.BookDAO;
import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.app.domain.models.entities.Book;
import org.tkit.app.rs.v1.mappers.BookMapper;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.models.criteria.BookSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.exceptions.RestException;

@ApplicationScoped
@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Books REST")
public class BookRestController {

    private static final String NOT_FOUND = "Book not found.";

    @Inject
    BookDAO bookDAO;

    @Inject
    BookMapper bookMapper;

    @GET
    @Path("/{bookIsbn}}")
    @Operation(operationId = "getBookByIsbn", description = "Gets book by ISBN")
    public Response getBookById(@PathParam("bookIsbn") Long bookIsbn) {
        Book book = bookDAO.findById(bookIsbn);
        if (book == null) {
            throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
        }
        return Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(bookMapper.mapToDTO(book))
                .build();
    }

    @GET
    @Operation(operationId = "getBookByCriteria", description = "Gets book by criteria")
    public Response getBookByCriteria(@BeanParam BookSearchCriteriaDTO bookSearchCriteriaDTO) {

        BookSearchCriteria bookSearchCriteria = bookMapper.mapToSearchCriteria(bookSearchCriteriaDTO);

        PageResult<Book> books = bookDAO.searchByCriteria(bookSearchCriteria);

        return Response.ok(bookMapper.mapToPageResultDTO(books))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createBook", description = "Adds new book")
    public Response createBook(BookDTO bookDTO) {

        Book book = bookMapper.mapToEntity(bookDTO);

        return Response.status(Response.Status.CREATED)
                .entity(bookMapper.mapToDTO(bookDAO.create(book)))
                .build();
    }

    @PUT
    @Path("/{bookIsbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateBook", description = "Updates book info")
    public Response updateBook(@PathParam("bookIsbn") Long bookIsbn, @Valid BookDTO bookDTO) {

        Book book = bookDAO.findById(bookIsbn);

        if (Objects.nonNull(book)) {
            bookMapper.updateBookFromDto(bookDTO, book);
            return Response.status(Response.Status.CREATED)
                    .entity(bookMapper.mapToDTO(bookDAO.update(book)))
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }

    @DELETE
    @Path("/{bookIsbn}")
    @Operation(operationId = "deleteBook", description = "Removes book")
    public Response deleteBook(@PathParam("bookIsbn") Long bookIsbn) {

        Book book = bookDAO.findById(bookIsbn);

        if (Objects.nonNull(book)) {
            bookDAO.delete(book);
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }
}
