package org.tkit.app.rs.v1.services;

import org.tkit.app.domain.daos.AuthorDAO;
import org.tkit.app.domain.daos.BookDAO;
import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.domain.models.entities.Book;
import org.tkit.app.rs.v1.mappers.BookMapper;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.models.criteria.BookSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.exceptions.RestException;
import org.tkit.quarkus.rs.models.PageResultDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class BookServiceImpl {

    @Inject
    BookDAO bookDAO;

    @Inject
    BookMapper bookMapper;

    @Inject
    AuthorDAO authorDAO;

    private static final String NOT_FOUND = "Book not found.";

    public BookDTO getBookById(String id) {

        Book book = bookDAO.findById(id);

        if (Objects.nonNull(book)) {
            return bookMapper.mapToDTO(book);
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }

    public PageResultDTO<BookDTO> getBookByCriteria(BookSearchCriteriaDTO bookSearchCriteriaDTO) {

        BookSearchCriteria bookSearchCriteria = bookMapper.mapToSearchCriteria(bookSearchCriteriaDTO);

        PageResult<Book> books = bookDAO.searchByCriteria(bookSearchCriteria);

        return bookMapper.mapToPageResultDTO(books);
    }

    public BookDTO createBook(BookDTO bookDTO) {

        Author author = authorDAO.findById(bookDTO.getBookAuthor().getId());

        Book book = bookMapper.mapToEntity(bookDTO);

        book.setBookAuthor(author);

        return bookMapper.mapToDTO(bookDAO.create(book));
    }

    public BookDTO updateBook(String bookIsbn, BookDTO bookDTO) {

        Book book = bookDAO.findById(bookIsbn);

        if (Objects.nonNull(book)) {
            bookMapper.updateBookFromDto(bookDTO, book);
            return bookMapper.mapToDTO(bookDAO.update(book));
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }

    public Response deleteBook(String id) {

        Book book = bookDAO.findById(id);

        if (Objects.nonNull(book)) {
            bookDAO.delete(book);
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }
}
