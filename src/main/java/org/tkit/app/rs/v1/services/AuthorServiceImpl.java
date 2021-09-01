package org.tkit.app.rs.v1.services;

import org.tkit.app.domain.daos.AuthorDAO;
import org.tkit.app.domain.models.criteria.AuthorSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.rs.v1.mappers.AuthorMapper;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.criteria.AuthorSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.exceptions.RestException;
import org.tkit.quarkus.rs.models.PageResultDTO;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Objects;

public class AuthorServiceImpl {

    private static final String NOT_FOUND = "Author not found.";

    @Inject
    AuthorDAO authorDAO;

    @Inject
    AuthorMapper authorMapper;

    public AuthorDTO getAuthorById(Long id) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            return authorMapper.mapToDTO(author);
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }

    public PageResultDTO<AuthorDTO> getAuthorByCriteria(AuthorSearchCriteriaDTO authorSearchCriteriaDTO) {

        AuthorSearchCriteria authorSearchCriteria = authorMapper.mapToSearchCriteria(authorSearchCriteriaDTO);

        PageResult<Author> authors = authorDAO.searchByCriteria(authorSearchCriteria);

        return authorMapper.mapToPageResultDTO(authors);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {

        Author author = authorMapper.mapToEntity(authorDTO);

        return authorMapper.mapToDTO(authorDAO.create(author));
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            authorMapper.updateAuthorFromDto(authorDTO, author);
            return authorMapper.mapToDTO(authorDAO.update(author));
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);

    }

    public Response deleteAuthor(Long id) {

        Author author = authorDAO.findById(id);

        if (Objects.nonNull(author)) {
            authorDAO.delete(author);
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
        throw new RestException(Response.Status.NOT_FOUND, Response.Status.NOT_FOUND, NOT_FOUND);
    }
}
