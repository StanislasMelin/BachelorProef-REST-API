package com.odisee.bibliotheek.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.odisee.bibliotheek.dto.BookRestModel;
import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import com.odisee.bibliotheek.model.BookContent;
import com.odisee.bibliotheek.service.BookService;
import com.odisee.bibliotheek.service.FileStorageService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.odisee.bibliotheek.repository.BookRepository;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.exception.BookNotFoundException;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.model.Author;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor // This will create a constructor for all the needed dependencies
public class BookController {
    // Dependency injection in Spring Boot
    @Autowired
    private final BookService bookService;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/books")
    CollectionModel<EntityModel<Book>> all() {

        List<EntityModel<Book>> employees = bookService.findAll().stream()
                .map(book -> EntityModel.of(book,
                        linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                        linkTo(methodOn(BookController.class).all()).withRel("books")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    // end::get-aggregate-root[]
    @PostMapping("/books")
    EntityModel<Book> newBook(@RequestBody BookRestModel newBook) {
        Book book = bookService.save(newBook);

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    // Single item
    @GetMapping("/books/{id}")
    EntityModel<Book> one(@PathVariable Long id) {

        Book book = bookService.findById(id);

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @PutMapping("/books/{id}")
    EntityModel<Book> updateBook(@RequestBody BookRestModel newBook, @PathVariable Long id) {
         Book book = bookService.update(id, newBook);

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }




}
