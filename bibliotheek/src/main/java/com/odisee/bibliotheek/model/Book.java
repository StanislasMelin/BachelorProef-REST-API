package com.odisee.bibliotheek.model;

import jakarta.persistence.*;

import lombok.*;

import java.util.Set;

/*
    Book: The main object of our project.
    We use the Entity annotation to signal this is an entity in our database.
    This object gets mapped to a table in our database.
    We also use lombok to generate all the boring getters/setters/equals/toString
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Book {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @NonNull private String title;
    @NonNull private int pages;
    @NonNull private String isbn;
    @NonNull private String year;
    @NonNull private String language;
    // Author field - we must add annotations to define the relationship between those two objects.
    @ManyToMany(targetEntity = Author.class, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns=@JoinColumn(name="authors"))
    @NonNull private Set authors;
}