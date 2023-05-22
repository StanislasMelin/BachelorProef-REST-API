package com.odisee.bibliotheek.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.odisee.bibliotheek.service.AuthorService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.odisee.bibliotheek.repository.BookRepository;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import com.odisee.bibliotheek.model.Author;
import com.odisee.bibliotheek.model.Author;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
    This is the AuthorController. The CRUD endpoints for
    the authors are defined in this file.
 */

@RestController
@RequiredArgsConstructor // This will create a constructor for all the needed dependencies
public class AuthorController {
    @Autowired
    private final AuthorService authorService;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/authors")
    CollectionModel<EntityModel<Author>> all() {

        List<EntityModel<Author>> employees = authorService.findAll().stream()
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(AuthorController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(AuthorController.class).all()).withRel("authors")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(AuthorController.class).all()).withSelfRel());
    }

    // end::get-aggregate-root[]
    @PostMapping("/authors")
    EntityModel<Author> newAuthor(@RequestBody Author newAuthor) {
        Author author = authorService.save(newAuthor);

        return EntityModel.of(author, //
                linkTo(methodOn(AuthorController.class).one(author.getId())).withSelfRel(),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"));
    }

    // Single item
    @GetMapping("/authors/{id}")
    EntityModel<Author> one(@PathVariable Long id) {
        Author author = authorService.findById(id);

        return EntityModel.of(author, //
                linkTo(methodOn(AuthorController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"));
    }

    @PutMapping("/authors/{id}")
    EntityModel<Author> replaceAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        Author author =  authorService.update(id, newAuthor);

        return EntityModel.of(author, //
                linkTo(methodOn(AuthorController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"));
    }

    @DeleteMapping("/authors/{id}")
    void deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
    }
}