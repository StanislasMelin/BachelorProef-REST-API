package com.odisee.bibliotheek.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BookContent {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @OneToOne(targetEntity = Book.class)
    @NonNull private Book book;
    // This is the filename
    @NonNull private String name;
    // This is the filetype: should be .pdf
    @NonNull private String type;
    @Lob
    @NonNull private byte[] data;
}
