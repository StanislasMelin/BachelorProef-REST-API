package com.odisee.bibliotheek.database;

import com.odisee.bibliotheek.model.Author;
import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.repository.AuthorRepository;
import com.odisee.bibliotheek.repository.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository, AuthorRepository authorRepository) {

        return args -> {
            // Defining authors
            Author asimov = new Author("Isaac", "Asimov");
            Author orwell = new Author("George", "Orwell");
            Author klanbik = new Author("Steve", "Klabnik");
            Author nichols = new Author("Caroll", "Nichols");

            // saving authors
            log.info("Preloading " + authorRepository.save(asimov));
            log.info("Preloading " + authorRepository.save(orwell));
            log.info("Preloading " + authorRepository.save(klanbik));
            log.info("Preloading " + authorRepository.save(nichols));

            //Defining authorsets
            Set<Author> asimovSet = new HashSet();
            asimovSet.add(asimov);

            Set<Author> klabnik_nichols = new HashSet();
            klabnik_nichols.add(klanbik);
            klabnik_nichols.add(nichols);

            // saving books
            log.info("Preloading " + bookRepository.save(new Book("Foundation and Earth", 355, "123456789", "2001", "English", asimovSet)));
            log.info("Preloading " + bookRepository.save(new Book("Prelude to Foundation", 355, "123456789", "2001", "English", asimovSet)));
            log.info("Preloading " + bookRepository.save(new Book("The Rust Programming Language", 600, "123456789", "2018", "English", klabnik_nichols)));
        };
    }
}
