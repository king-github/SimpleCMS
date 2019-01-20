package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.form.panel.ArticleSearchForm;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleService {
    Page<Article> getAllArticles(Pageable pageable);

    Page<Article> getAllArticles(ArticleSearchForm articleSearchForm, Pageable pageable);

    Page<Article> getAllArticles(Example<Article> example, Pageable pageable);

    Page<Article> findArticleByAuthor(Long id, Pageable pageable);

    Page<Article> getAllPublishedArticles(Pageable pageable);

    Page<Article> findPublishedArticleByAuthor(Long id, Pageable pageable);

    Page<Article> findPublishedArticleByTag(Long id, Pageable pageable);

    Page<Article> findPublishedArticleBySection(Long id, Pageable pageable);

    Page<Article> findArticlesByExample(Article article, Pageable pageable);

    void addArticle(Article article);

    Article findArticle(Long id);

    Optional<Article> findArticleById(Long id);

    int deleteArticles(Iterable<Long> ids);

    Article save(Article toArticle);
}
