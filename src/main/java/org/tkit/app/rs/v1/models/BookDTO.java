package org.tkit.app.rs.v1.models;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.tkit.app.domain.models.entities.Author;
import org.tkit.app.domain.models.enums.BookCategory;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;

@Getter
@Setter
public class BookDTO  extends TraceableDTO {

    @Schema(description = "The book's ISBN number")
    private String isbn;

    @Schema(description = "The book's title")
    private String title;

    @Schema(description = "The book's number of pages")
    private String pages;

    @Schema(description = "The book's category")
    private BookCategory bookCategory;

    @Schema(description = "The book's author")
    private Author author;

}
