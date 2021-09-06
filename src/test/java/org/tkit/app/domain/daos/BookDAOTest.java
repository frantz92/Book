package org.tkit.app.domain.daos;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.quarkus.jpa.exceptions.DAOException;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

@QuarkusTest
class BookDAOTest {

    @Test
    void shouldThrowDAOExceptionWhenTryFindBookByCriteriaEqualNull() {

        /* Given */
        BookDAO bookDAO = new BookDAO() {
            @Override
            protected EntityManager getEntityManager() {
                return null;
            }
        };

        /* When */
        DAOException thrownException = Assertions.assertThrows(DAOException.class, () ->
                bookDAO.searchByCriteria(null));

        /* Then */
        assertThat(thrownException.key).isEqualTo(BookDAO.ErrorKeys.ERROR_FIND_BOOKS_SEARCH_CRITERIA_REQUIRED);
    }

    @Test
    void shouldThrowDAOExceptionWhenTryFindBookByEmptyCriteria() {

        /* Given */
        BookDAO bookDAO = new BookDAO() {
            @Override
            protected EntityManager getEntityManager() {
                return null;
            }
        };

        BookSearchCriteria bookSearchCriteria = new BookSearchCriteria();

        /* When */
        DAOException thrownException = Assertions.assertThrows(DAOException.class, () ->
                bookDAO.searchByCriteria(bookSearchCriteria));

        /* Then */
        assertThat(thrownException.key).isEqualTo(BookDAO.ErrorKeys.ERROR_FIND_BOOKS_BY_CRITERIA);
    }
}
