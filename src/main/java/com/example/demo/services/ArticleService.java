package com.example.demo.services;

import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles();

    void addArticle(Article article);

    Article findArticle(Long id);
}
