package org.tkit.app.domain.models.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorSearchCriteria {

    private Long authorId;

    private String authorName;

    private String authorSurname;

    private Integer authorAge;

    private Integer pageNumber;

    private Integer pageSize;

}
