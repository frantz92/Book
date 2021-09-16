package org.tkit.app.rs.v1.models;

import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.tkit.app.domain.models.enums.BookCategory;

import javax.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BookDTO extends TraceableDTO {

    @Schema(description = "The book's ISBN number")
    private String bookIsbn;

    @NotNull
    @Schema(description = "The book's title")
    private String bookTitle;

    @Schema(description = "The book's number of pages")
    private Integer bookPages;

    @NotNull
    @Schema(description = "The book's category")
    private BookCategory bookCategory;

    @NotNull
    @Schema(description = "The book's author")
    private AuthorDTO bookAuthor;

    @Override
    public String toString() {
        return "BookDTO:" + getId();
    }

}
