package com.beyond.basic.b2_board.Repository;

import com.beyond.basic.b2_board.Domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryInterface{

    void save(Author author);
    List<Author> findAll();
    Optional<Author> findById(Long id);
    Optional<Author> findByEmail(String email);
    void delete(Long id);
}
