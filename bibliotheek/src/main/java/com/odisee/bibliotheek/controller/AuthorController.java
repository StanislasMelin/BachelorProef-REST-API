package com.odisee.bibliotheek.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.odisee.bibliotheek.exception.AuthorNotFoundException;
import com.odisee.bibliotheek.model.Author;
import com.odisee.bibliotheek.model.Author;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor // This will create a constructor for all the needed dependencies
public class AuthorController {
    // Dependency injection in Spring Boot
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/authors")
    CollectionModel<EntityModel<Author>> all() {

        List<EntityModel<Author>> employees = authorRepository.findAll().stream()
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(AuthorController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(AuthorController.class).all()).withRel("authors")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(AuthorController.class).all()).withSelfRel());
    }

    // end::get-aggregate-root[]
    @PostMapping("/authors")
    Author newAuthor(@RequestBody Author newAuthor) {
        return authorRepository.save(newAuthor);
    }

    // Single item
    @GetMapping("/authors/{id}")
    EntityModel<Author> one(@PathVariable Long id) {

        Author author = authorRepository.findById(id) //
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return EntityModel.of(author, //
                linkTo(methodOn(AuthorController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors"));
    }

    @PutMapping("/authors/{id}")
    Author replaceAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {

        return authorRepository.findById(id)
                .map(author -> {
                    author.setFirstName(newAuthor.getFirstName());
                    author.setLastName(newAuthor.getLastName());
                    return authorRepository.save(author);
                })
                .orElseGet(() -> {
                    newAuthor.setId(id);
                    return authorRepository.save(newAuthor);
                });
    }

    @DeleteMapping("/authors/{id}")
    void deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }
}
