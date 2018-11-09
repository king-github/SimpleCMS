package com.example.demo.services;

import com.example.demo.dto.AuthorWithQuantityDto;
import com.example.demo.entity.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();

    List<AuthorWithQuantityDto> getAllAuthorsWithQuantity();
}
