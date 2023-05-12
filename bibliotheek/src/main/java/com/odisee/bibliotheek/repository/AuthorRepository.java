package com.odisee.bibliotheek.repository;

import com.odisee.bibliotheek.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
