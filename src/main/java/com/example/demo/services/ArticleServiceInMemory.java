package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleService;
import com.example.demo.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceInMemory implements ArticleService {

    private final int NUM_OF_ARTICLES = 10;

    private List<Article> articles = new ArrayList<>();

    public ArticleServiceInMemory() {

        for (int i=1; i< NUM_OF_ARTICLES; i++)
            addArticle(new Article("Title "+i, "Lorem ipsum ... "+i, "Content Lorem ipsuma dsnfsd fsdn fdf", "Author "+i));

    }

    @Override
    public List<Article> getAllArticles() {

        return articles;
    }

    @Override
    public void addArticle(Article article) {
        articles.add(article);
    }

    @Override
    public Article findArticle(Long id) {

        return articles.stream()
                       .filter(article -> article.getId() == id)
                       .findFirst()
                       .orElseThrow(() -> new NotFoundException("Article not found"));

    }

}
