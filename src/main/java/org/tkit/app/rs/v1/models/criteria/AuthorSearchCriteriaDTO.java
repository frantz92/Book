package org.tkit.app.rs.v1.models.criteria;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Getter
@Setter
public class AuthorSearchCriteriaDTO extends PageCriteriaDTO {

    @QueryParam("authorId")
    private Long authorId;

    @QueryParam("authorName")
    private String authorName;

    @QueryParam("authorSurname")
    private String authorSurname;

    @QueryParam("authorAge")
    private Integer age;

}
