package org.tkit.app.domain.daos;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.tkit.app.domain.models.criteria.AuthorSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AuthorDAO extends AbstractDAO<Author> {

    enum ErrorKeys {
        ERROR_FIND_AUTHORS_BY_CRITERIA,
        ERROR_FIND_AUTHORS_SEARCH_CRITERIA_REQUIRED
    }

    /**
     * Finds a {@link PageResult} of {@link Author} matching the given {@link AuthorSearchCriteria}.
     *
     * @param criteria the {@link AuthorSearchCriteria}
     * @return the {@link PageResult} of {@link Author}
     */

    public PageResult<Author> searchByCriteria(AuthorSearchCriteria criteria) {
        if (criteria == null) {
            throw new DAOException(AuthorDAO.ErrorKeys.ERROR_FIND_AUTHORS_SEARCH_CRITERIA_REQUIRED, new NullPointerException());
        }
        try {
            CriteriaQuery<Author> cq = criteriaQuery();
            Root<Author> root = cq.from(Author.class);
            List<Predicate> predicates = new ArrayList<>();
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            if (criteria.getAuthorName() != null) {
                predicates.add(cb.equal(root.get("authorName"), criteria.getAuthorName()));
            }
            if (!predicates.isEmpty()) {
                cq.where(predicates.toArray(new Predicate[0]));
            }
            return createPageQuery(cq, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
        } catch (Exception e) {
            throw new DAOException(AuthorDAO.ErrorKeys.ERROR_FIND_AUTHORS_BY_CRITERIA, e);
        }
    }
}
