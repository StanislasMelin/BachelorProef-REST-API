package com.odisee.bibliotheek.service;

import com.odisee.bibliotheek.dto.BookRestModel;
import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import com.odisee.bibliotheek.exception.BookNotFoundException;
import com.odisee.bibliotheek.model.Author;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author save(Author model) {
        return authorRepository.save(model);
    }

    public Author findById(long id) {
        return authorRepository.findById(id) //
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Author update(long id, Author newAuthor) {
        return  authorRepository.findById(id)
                .map(toUpdate -> {
                    toUpdate.setFirstName(newAuthor.getFirstName());
                    toUpdate.setLastName(newAuthor.getLastName());
                    return authorRepository.save(toUpdate);
                })
                .orElseGet(() -> {
                    newAuthor.setId(id);
                    return authorRepository.save(newAuthor);
                });
    }

    public void delete(long id) {
        authorRepository.deleteById(id);
    }
}