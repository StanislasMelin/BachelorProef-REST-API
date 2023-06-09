package com.odisee.bibliotheek.dto;

import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import com.odisee.bibliotheek.model.Author;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.repository.AuthorRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/*
    This class is used to post or put books. It abstracts
    having to deal with sending authors along the request.
    User can send an array of author id's instead.
    Heavy use of lombok to abstract the boring stuff.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Slf4j
public class BookRestModel {
    private long id;
    @NonNull private String title;
    @NonNull private int pages;
    @NonNull private String isbn;
    @NonNull private String year;
    @NonNull private String language;
    @NonNull private long[] authors_id;

    public Book toModel(AuthorRepository authorRepository) {
        // Creating new books
        Book book = new Book(
                this.getTitle(),
                this.getPages(),
                this.getIsbn(),
                this.getYear(),
                this.getLanguage(),
                this.getAuthors(authorRepository));

        return book;
    }

    public Set<Author> getAuthors(AuthorRepository authorRepository) {
        //Defining authorsets
        Set<Author> authors = new HashSet();
        for (int i = 0;i<this.getAuthors_id().length;i++) {
            long authorId = (long)this.getAuthors_id()[i];
            Author newAuthor = authorRepository
                    .findById(authorId)
                    .orElseThrow(() -> new AuthorNotFoundException(authorId));
            authors.add(newAuthor);
        }

        return authors;
    }
}
