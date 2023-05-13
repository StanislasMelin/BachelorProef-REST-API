package com.odisee.bibliotheek.exception;

public class BookContentNotFoundException extends RuntimeException  {
    public BookContentNotFoundException(Long id) {
        super("Could not find content for book " + id);
    }
}
