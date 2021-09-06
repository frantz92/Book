package org.tkit.app.domain.daos;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tkit.app.domain.models.criteria.AuthorSearchCriteria;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class AuthorDAOTest {
    @Test
    void shouldThrowDAOExceptionWhenTryFindAuthorByCriteriaEqualNull() {

        /* Given */
        AuthorDAO authorDAO = new AuthorDAO() {
            @Override
            protected EntityManager getEntityManager() {
                return null;
            }
        };

        /* When */
        DAOException thrownException = Assertions.assertThrows(DAOException.class, () ->
                authorDAO.searchByCriteria(null));

        /* Then */
        assertThat(thrownException.key).isEqualTo(AuthorDAO.ErrorKeys.ERROR_FIND_AUTHORS_SEARCH_CRITERIA_REQUIRED);
    }

    @Test
    void shouldThrowDAOExceptionWhenTryFindAuthorByEmptyCriteria() {

        /* Given */
        AuthorDAO authorDAO = new AuthorDAO() {
            @Override
            protected EntityManager getEntityManager() {
                return null;
            }
        };

        AuthorSearchCriteria authorSearchCriteria = new AuthorSearchCriteria();

        /* When */
        DAOException thrownException = Assertions.assertThrows(DAOException.class, () ->
                authorDAO.searchByCriteria(authorSearchCriteria));

        /* Then */
        assertThat(thrownException.key).isEqualTo(AuthorDAO.ErrorKeys.ERROR_FIND_AUTHORS_BY_CRITERIA);
    }
}
