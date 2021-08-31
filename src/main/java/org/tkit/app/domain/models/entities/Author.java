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

    @Column(name = "Id", nullable = false)
    private Long authorId;

    @Column(name = "Name", nullable = false)
    private String authorName;

    @Column(name = "Surname", nullable = false)
    private String authorSurname;

    @Column(name = "Age", nullable = false)
    @Transient
    private Integer authorAge;

    @Override
    public boolean equals (Object o) {
        return super.equals (o);
    }

    @Override
    public int hashCode () {
        return super.hashCode ();
    }
}