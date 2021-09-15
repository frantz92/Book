package org.tkit.app.domain.daos;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.app.domain.models.entities.Book;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookDAO extends AbstractDAO<Book> {

    enum ErrorKeys {
        ERROR_FIND_BOOKS_BY_CRITERIA,
        ERROR_FIND_BOOKS_SEARCH_CRITERIA_REQUIRED
    }

    /**
     * Finds a {@link PageResult} of {@link Book} matching the given {@link BookSearchCriteria}.
     *
     * @param criteria the {@link BookSearchCriteria}
     * @return the {@link PageResult} of {@link Book}
     */

    public PageResult<Book> searchByCriteria(BookSearchCriteria criteria) {
        if (criteria == null) {
            throw new DAOException(ErrorKeys.ERROR_FIND_BOOKS_SEARCH_CRITERIA_REQUIRED, new NullPointerException());
        }
        try {
            CriteriaQuery<Book> bookCQ = criteriaQuery();
            Root<Book> bookRoot = bookCQ.from(Book.class);
            List<Predicate> bookPredicates = new ArrayList<>();
            CriteriaBuilder bookCB = getEntityManager().getCriteriaBuilder();

            if (criteria.getBookTitle() != null && !criteria.getBookTitle().isEmpty()) {
                bookPredicates.add(bookCB.like(bookCB.lower(bookRoot.get("bookTitle")), criteria.getBookTitle().toLowerCase() + "%"));
            }

            if (criteria.getBookISBN() != null) {
                bookPredicates.add(bookCB.equal(bookRoot.get("bookIsbn"), criteria.getBookISBN()));
            }

            if (criteria.getBookPages() != null) {
                bookPredicates.add(bookCB.equal(bookRoot.get("bookPages"), criteria.getBookPages()));
            }

            if (criteria.getBookCategory() != null) {
                bookPredicates.add(bookCB.equal(bookRoot.get("bookCategory"), criteria.getBookCategory()));
            }

            if (criteria.getBookAuthorName() != null && !criteria.getBookAuthorName().isEmpty()) {
                bookPredicates.add(bookCB.like(bookCB.lower(bookRoot.get("bookAuthor").get("authorName")),
                        criteria.getBookAuthorName().toLowerCase() + "%"));
            }

            if (criteria.getBookAuthorSurname() != null && !criteria.getBookAuthorSurname().isEmpty()) {
                bookPredicates.add(bookCB.like(bookCB.lower(bookRoot.get("bookAuthor").get("authorSurname")),
                        criteria.getBookAuthorSurname().toLowerCase() + "%"));
            }

            if (!bookPredicates.isEmpty()) {
                bookCQ.where(bookPredicates.toArray(new Predicate[0]));
            }
            return createPageQuery(bookCQ, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
        } catch (Exception e) {
            throw new DAOException(ErrorKeys.ERROR_FIND_BOOKS_BY_CRITERIA, e);
        }
    }
}
