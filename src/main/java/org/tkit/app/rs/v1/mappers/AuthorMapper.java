package org.tkit.app.rs.v1.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tkit.app.domain.models.criteria.AuthorSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.criteria.AuthorSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.models.PageResultDTO;

@Mapper
public interface AuthorMapper {

    AuthorDTO mapToDTO(Author modelAuthor);

    Author mapToEntity(AuthorDTO author);

    AuthorSearchCriteria mapToSearchCriteria(AuthorSearchCriteriaDTO searchCriteria);

    PageResultDTO<AuthorDTO> mapToPageResultDTO(PageResult<Author> page);


    @Mapping(target = "id", ignore = true)
    Author updateAuthorFromDto(AuthorDTO authorDTO, @MappingTarget Author author);

}
