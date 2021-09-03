package org.tkit.app.domain.models.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.tkit.app.domain.models.enums.BookCategory;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "BOOKS")
public class Book extends TraceableEntity {

    @Column(name = "ISBN", nullable = false, unique = true)
    private String bookIsbn;

    @Column(name = "TITLE", nullable = false)
    private String bookTitle;

    @Column(name = "PAGES", nullable = false)
    private Integer bookPages;

    @Column(name = "BOOK_CATEGORY", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID") //TODO add nullable
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Author bookAuthor;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
