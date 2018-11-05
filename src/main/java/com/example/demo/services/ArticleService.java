package com.example.demo.entity;

import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles();

    List<Article> findArticleByAuthor(Long id);

    List<Article> findArticleByTag(Long id);

    void addArticle(Article article);

    Article findArticle(Long id);
}
