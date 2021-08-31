package org.tkit.app.rs.v1.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tkit.app.domain.models.criteria.BookSearchCriteria;
import org.tkit.app.domain.models.entities.Book;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.models.criteria.BookSearchCriteriaDTO;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;

@Mapper(componentModel = "cdi", uses = OffsetDateTimeMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    BookDTO mapToDTO(Book modelBook);

    Book mapToEntity(BookDTO book);

    BookSearchCriteria mapToSearchCriteria(BookSearchCriteriaDTO searchCriteria);

    PageResultDTO<BookDTO> mapToPageResultDTO(PageResult<Book> page);

    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(BookDTO bookDTO, @MappingTarget Book book);

}
