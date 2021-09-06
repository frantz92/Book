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
            CriteriaQuery<Author> authorCQ = criteriaQuery();
            Root<Author> authorRoot = authorCQ.from(Author.class);
            List<Predicate> authorPredicates = new ArrayList<>();
            CriteriaBuilder authorCB = getEntityManager().getCriteriaBuilder();

            if (criteria.getAuthorName() != null) {
                authorPredicates.add(authorCB.equal(authorRoot.get("authorName"), criteria.getAuthorName()));
            }

            if (criteria.getAuthorSurname() != null) {
                authorPredicates.add(authorCB.equal(authorRoot.get("authorSurname"), criteria.getAuthorSurname()));
            }

            if (criteria.getAuthorAge() != null) {
                authorPredicates.add(authorCB.equal(authorRoot.get("authorAge"), criteria.getAuthorAge()));
            }

            if (!authorPredicates.isEmpty()) {
                authorCQ.where(authorPredicates.toArray(new Predicate[0]));
            }
            return createPageQuery(authorCQ, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
        } catch (Exception e) {
            throw new DAOException(AuthorDAO.ErrorKeys.ERROR_FIND_AUTHORS_BY_CRITERIA, e);
        }
    }
}
