package org.tkit.app.rs.v1.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.tkit.quarkus.rs.models.TraceableDTO;

import javax.validation.constraints.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AuthorDTO extends TraceableDTO {

    @NotBlank
    @Schema(description = "Authors name")
    private String authorName;

    @NotBlank
    @Schema(description = "Authors surname")
    private String authorSurname;

    @NotNull
    @Schema(description = "Authors age")
    private Integer authorAge;

    @Override
    public String toString() {
        return "AuthorDTO:" + getId();
    }

}
