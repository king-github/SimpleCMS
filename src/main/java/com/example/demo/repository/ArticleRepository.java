package com.example.demo.repository;


import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    Page<Article> findArticleByAuthorId(Long id, Pageable pageable);

    @Query("select a from Article a join a.tags t where t.id = ?1")
    Page<Article> findArticleByTagId(Long id, Pageable pageable);
}
