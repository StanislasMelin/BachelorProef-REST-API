package com.odisee.bibliotheek.service;

import com.odisee.bibliotheek.dto.BookRestModel;
import com.odisee.bibliotheek.exception.BookNotFoundException;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(BookRestModel model) {
        return bookRepository.save(model.toModel(authorRepository));
    }

    public Book findById(long id) {
        return bookRepository.findById(id) //
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book update(long id, BookRestModel newBook) {
        return bookRepository.findById(id)
                .map(toUpdate -> {
                    toUpdate.setTitle(newBook.getTitle());
                    toUpdate.setPages(newBook.getPages());
                    toUpdate.setIsbn(newBook.getIsbn());
                    toUpdate.setYear(newBook.getYear());
                    toUpdate.setLanguage(newBook.getLanguage());
                    toUpdate.setAuthors(newBook.getAuthors(authorRepository));

                    return bookRepository.save(toUpdate);

                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook.toModel(authorRepository));
                });
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }


}