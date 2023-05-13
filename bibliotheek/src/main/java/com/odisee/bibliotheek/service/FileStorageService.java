package com.odisee.bibliotheek.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.odisee.bibliotheek.exception.BookContentNotFoundException;
import com.odisee.bibliotheek.exception.BookNotFoundException;

import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.model.BookContent;

import com.odisee.bibliotheek.repository.BookContentRepository;
import com.odisee.bibliotheek.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileStorageService {
    @Autowired
    private BookContentRepository bookContentRepository;
    @Autowired
    private BookRepository bookRepository;

    // This function will fetch a book and create a bookcontent object
    // To store it in the database.
    public BookContent store(Long bookId, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("Searching for book...");
        // Fetching book. Throwing 404 if book is not found.
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(()-> new BookNotFoundException(bookId));
        log.info("Book found: " + book.toString());

        BookContent bookContent = new BookContent(
            book,
            fileName,
            file.getContentType(),
            file.getBytes()
        );
        log.info("Saving book content...");
        // Saving it to DB
        return bookContentRepository.save(bookContent);
    }

    public BookContent getContent(String id) {
        return bookContentRepository.findById(id).orElse(null);
    }

    public BookContent getContent(Long bookId) {
        log.info("Searching for book...");
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book == null) {
            throw new BookNotFoundException(bookId);
        }else {
            log.info("Book found: " + book.toString());
        }

        return bookContentRepository.findDistinctByBook(book);
    }

    public List<BookContent> getAllFiles() {
        return bookContentRepository.findAll();
    }

    public void deleteContent(Long id) {
        BookContent bc = this.getContent(id);
        if(bc == null) return;

        bookContentRepository.deleteById(bc.getId());
    }
}
