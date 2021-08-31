package org.tkit.app.rs.v1.models;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.tkit.quarkus.rs.models.TraceableDTO;

@Getter
@Setter
public class AuthorDTO extends TraceableDTO {

    @Schema(description = "Authors ID")
    private Long authorId;

    @Schema(description = "Authors name")
    private String authorName;

    @Schema(description = "Authors surname")
    private String authorSurname;

    @Schema(description = "Authors age")
    private Integer authorAge;

}
