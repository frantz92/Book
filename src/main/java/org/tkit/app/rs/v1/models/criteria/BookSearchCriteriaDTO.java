package org.tkit.app.rs.v1.models.criteria;

import lombok.Getter;
import lombok.Setter;
import org.tkit.app.domain.models.enums.BookCategory;

import javax.ws.rs.QueryParam;

@Getter
@Setter
public class BookSearchCriteriaDTO extends PageCriteriaDTO {

    @QueryParam("bookISBN")
    private String bookISBN;

    @QueryParam("bookTitle")
    private String bookTitle;

    @QueryParam("bookPages")
    private Integer bookPages;

    @QueryParam("bookCategory")
    private BookCategory bookCategory;

    @QueryParam("bookAuthorName")
    private String bookAuthorName;

    @QueryParam("bookAuthorSurname")
    private String bookAuthorSurname;

}
