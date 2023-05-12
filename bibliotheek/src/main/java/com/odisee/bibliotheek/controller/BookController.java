package com.odisee.bibliotheek.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.odisee.bibliotheek.dto.BookRestModel;
import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.odisee.bibliotheek.repository.BookRepository;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.exception.BookNotFoundException;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.model.Author;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor // This will create a constructor for all the needed dependencies
public class BookController {
    // Dependency injection in Spring Boot
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/books")
    CollectionModel<EntityModel<Book>> all() {

        List<EntityModel<Book>> employees = bookRepository.findAll().stream()
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(BookController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(BookController.class).all()).withRel("books")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    // end::get-aggregate-root[]
    @PostMapping("/books")
    EntityModel<Book> newBook(@RequestBody BookRestModel newBook) {
        Book book = bookRepository.save(newBook.toModel(authorRepository));

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    // Single item
    @GetMapping("/books/{id}")
    EntityModel<Book> one(@PathVariable Long id) {

        Book book = bookRepository.findById(id) //
                .orElseThrow(() -> new BookNotFoundException(id));

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @PutMapping("/books/{id}")
    EntityModel<Book> replaceBook(@RequestBody BookRestModel newBook, @PathVariable Long id) {
         Book book = bookRepository.findById(id)
                .map(toUpdate -> {
                    toUpdate.setTitle(newBook.getTitle());
                    toUpdate.setPages(newBook.getPages());
                    toUpdate.setIsbn(newBook.getIsbn());
                    toUpdate.setYear(newBook.getYear());
                    toUpdate.setLanguage(newBook.getLanguage());
                    toUpdate.setAuthorSet(newBook.getAuthorSet(authorRepository));

                    return bookRepository.save(toUpdate);

                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook.toModel(authorRepository));
                });

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
