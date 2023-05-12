package com.odisee.bibliotheek.model;

import jakarta.persistence.*;

import lombok.*;

import java.util.Set;

/*
    Author: One of the tables in our DB
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
public class Author {
    private @Id @GeneratedValue Long id;
    @NonNull private String firstName;
    @NonNull private String lastName;
}
