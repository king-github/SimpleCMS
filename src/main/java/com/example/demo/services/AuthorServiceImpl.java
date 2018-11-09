package com.example.demo.services;

import com.example.demo.dto.AuthorWithQuantityDto;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {

        return authorRepository.findAll();
    }

    @Override
    public List<AuthorWithQuantityDto> getAllAuthorsWithQuantity() {

        return authorRepository.findAuthorsWithQuantity();
    }

}
