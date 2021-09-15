package org.tkit.app.domain.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "AUTHORS")
public class Author extends TraceableEntity {

    @Column(name = "AUTHOR_NAME", nullable = false)
    private String authorName;

    @Column(name = "AUTHOR_SURNAME", nullable = false)
    private String authorSurname;

    @Column(name = "AUTHOR_AGE", nullable = false)
    private Integer authorAge;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}