package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleService;
import com.example.demo.error.NotFoundException;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getAllArticles() {

        return articleRepository.findAll();
    }

    @Override
    public List<Article> findArticleByAuthor(Long id) {
        return articleRepository.findArticleByAuthorId(id);
    }

    @Override
    public List<Article> findArticleByTag(Long id) {
        return articleRepository.findArticleByTagId(id);
    }


    @Override
    public void addArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public Article findArticle(Long id) {

        return articleRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Article not found"));

    }

}
