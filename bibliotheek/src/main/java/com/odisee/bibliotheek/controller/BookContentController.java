package com.odisee.bibliotheek.controller;

import com.odisee.bibliotheek.exception.BookContentNotFoundException;
import com.odisee.bibliotheek.exception.BookNotFoundException;
import com.odisee.bibliotheek.model.BookContent;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.repository.BookRepository;
import com.odisee.bibliotheek.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
    This file contains the BookContentController.
    The endpoints for adding and getting book contents are
    defined here.
 */

@Slf4j
@RestController
@RequiredArgsConstructor // This will create a constructor for all the needed dependencies
public class BookContentController {
    // Dependency injection in Spring Boot
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/books/{id}/content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EntityModel<BookContent> uploadContent(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        BookContent bookContent = null;
        try {
            // Make asynchronous call to the service
            CompletableFuture<BookContent> completableFuture = fileStorageService.store(id, file);

            // Wait for it to be done calculating.. The heavy work happens in another thread.
            // The service can handle other requests in the meantime.
            CompletableFuture.allOf(completableFuture).join();

            // Get the result of the async computation
            bookContent = completableFuture.get();

        } catch(Exception ex) {
            log.error(ex.getMessage());
        }

        return EntityModel.of(bookContent, //
                linkTo(methodOn(BookController.class).one(bookContent.getBook().getId())).withSelfRel(),
                linkTo(methodOn(BookContentController.class).all()).withRel("books"));
    }

    @GetMapping("/books/{id}/content")
    public EntityModel<BookContent> one(@PathVariable Long id) {
        log.info("Fetching single book content...");
        BookContent bookContent = fileStorageService.getContent(id);
        if(bookContent == null) {
            throw new BookContentNotFoundException(id);
        }

        return EntityModel.of(bookContent, //
                linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
                linkTo(methodOn(BookContentController.class).all()).withRel("books"));
    }

    @GetMapping("/books/content")
    public CollectionModel<EntityModel<BookContent>> all() {
        log.info("Listing all book contents...");
        List<EntityModel<BookContent>> bookContents = fileStorageService.getAllFiles().stream()
                .map(bookContent -> EntityModel.of(bookContent,
                        linkTo(methodOn(BookController.class).one(bookContent.getBook().getId())).withSelfRel(),
                        linkTo(methodOn(BookContentController.class).all()).withRel("books")))
                .collect(Collectors.toList());

        return CollectionModel.of(bookContents, linkTo(methodOn(BookContentController.class).all()).withSelfRel());
    }

    @DeleteMapping("/books/{id}/content")
    void deleteBook(@PathVariable Long id) {
        fileStorageService.deleteContent(id);
    }
}
