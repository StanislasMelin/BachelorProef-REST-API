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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Cacheable(value = "authors")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "author", allEntries = true)
    })
    public Author save(Author model) {
        return authorRepository.save(model);
    }

    @Cacheable(value = "author")
    public Author findById(long id) {
        return authorRepository.findById(id) //
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "author", allEntries = true)
    })
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

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "author", allEntries = true)
    })
    public void delete(long id) {
        authorRepository.deleteById(id);
    }
}
