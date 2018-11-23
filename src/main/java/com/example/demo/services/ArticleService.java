package com.example.demo.services;

import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<Article> getAllArticles(Pageable pageable);

    Page<Article> findArticleByAuthor(Long id, Pageable pageable);

    Page<Article> getAllPublishedArticles(Pageable pageable);

    Page<Article> findPublishedArticleByAuthor(Long id, Pageable pageable);

    Page<Article> findPublishedArticleByTag(Long id, Pageable pageable);

    void addArticle(Article article);

    Article findArticle(Long id);
}
