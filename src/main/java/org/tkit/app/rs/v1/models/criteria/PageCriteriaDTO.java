package org.tkit.app.rs.v1.models.criteria;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Getter
@Setter
public class PageCriteriaDTO {

    @Min(0)
    @QueryParam("page")
    @DefaultValue("0")
    private Integer pageNumber;

    @Min(1)
    @Max(100)
    @QueryParam("size")
    @DefaultValue("100")
    private Integer pageSize;
}
