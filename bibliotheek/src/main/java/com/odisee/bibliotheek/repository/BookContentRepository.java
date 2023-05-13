package com.odisee.bibliotheek.repository;

import com.odisee.bibliotheek.model.Book;
import com.odisee.bibliotheek.model.BookContent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookContentRepository extends JpaRepository<BookContent, String> {
    @Transactional
    BookContent findDistinctByBook(Book book);
}
