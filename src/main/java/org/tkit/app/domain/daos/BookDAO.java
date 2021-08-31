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
            CriteriaQuery<Book> cq = criteriaQuery();
            Root<Book> root = cq.from(Book.class);
            List<Predicate> predicates = new ArrayList<>();
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            if (criteria.getBookAuthor() != null) {
                predicates.add(cb.equal(root.get("bookAuthor"), criteria.getBookAuthor()));
            }
            if (!predicates.isEmpty()) {
                cq.where(predicates.toArray(new Predicate[0]));
            }
            return createPageQuery(cq, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
        } catch (Exception e) {
            throw new DAOException(ErrorKeys.ERROR_FIND_BOOKS_BY_CRITERIA, e);
        }
    }
}
