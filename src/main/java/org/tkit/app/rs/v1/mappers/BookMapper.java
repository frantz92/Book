package org.tkit.app.rs.v1.mappers;

import org.mapstruct.*;
import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.domain.models.entities.Book;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.models.criteria.BookSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;

@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    Author map(AuthorDTO author);

    AuthorDTO map(Author author);

    BookDTO mapToDTO(Book book);

    @Mapping(target = "bookAuthor")
    Book mapToEntity(BookDTO book);

    BookSearchCriteria mapToSearchCriteria(BookSearchCriteriaDTO searchCriteria);

    PageResultDTO<BookDTO> mapToPageResultDTO(PageResult<Book> page);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "bookAuthor", ignore = true)
    void updateBookFromDto(BookDTO bookDTO, @MappingTarget Book book);

}
