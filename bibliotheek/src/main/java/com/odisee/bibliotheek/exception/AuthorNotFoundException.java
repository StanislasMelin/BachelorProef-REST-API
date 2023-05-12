package com.odisee.bibliotheek.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Long id) {
        super("Could not find author " + id);
    }
}
