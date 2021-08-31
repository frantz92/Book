package org.tkit.app.rs.v1.models;

import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.domain.models.enums.BookCategory;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BookDTO extends TraceableDTO {

    @Schema(description = "The book's ISBN number")
    private Long bookIsbn;

    @Schema(description = "The book's title")
    private String bookTitle;

    @Schema(description = "The book's number of pages")
    private Integer bookPages;

    @Schema(description = "The book's category")
    private BookCategory bookCategory;

    @Schema(description = "The book's author")
    private Author author;

    @Override
    public String toString() {
        return "BookDTO:" + bookIsbn;
    }
    
}
