package com.odisee.bibliotheek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.odisee.bibliotheek.model.Book;


public interface BookRepository extends JpaRepository<Book, Long> {

}
