package org.tkit.app.domain.models.criteria;

import lombok.Getter;
import lombok.Setter;
import org.tkit.app.domain.models.enums.BookCategory;

@Getter
@Setter
public class BookSearchCriteria {

    private Long bookISBN;

    private String bookTitle;

    private Integer bookPages;

    private BookCategory bookCategory;

    private String bookAuthorName;

    private String bookAuthorSurname;

    private Integer pageNumber;

    private Integer pageSize;

}
