package com.example.demo.repository;


import com.example.demo.dto.AuthorWithQuantityDto;
import com.example.demo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT new com.example.demo.dto.AuthorWithQuantityDto (au.id, au.firstname, au.lastname, count(a.author.id) as quantity) " +
           "FROM Author au " +
           "LEFT JOIN au.articles a " +
           "WHERE a.published = TRUE " +
           "GROUP BY au.id " +
           "ORDER BY quantity DESC")
    List<AuthorWithQuantityDto> findAuthorsWithQuantity();
}
