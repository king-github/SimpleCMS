package com.example.demo.services;

import com.example.demo.entity.Article;

import com.example.demo.error.NotFoundException;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Primary
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAll( pageable );
    }

    @Override
    public Page<Article> findArticleByAuthor(Long id, Pageable pageable) {
        return articleRepository.findArticleByAuthorId(id, pageable );
    }

    @Override
    public Page<Article> getAllPublishedArticles(Pageable pageable) {
        return articleRepository.findArticleByPublishedTrue(pageable);
    }

    @Override
    public Page<Article> findPublishedArticleByAuthor(Long id, Pageable pageable) {
        return articleRepository.findArticleByAuthorIdAndPublishedTrue(id, pageable );
    }

    @Override
    public Page<Article> findPublishedArticleByTag(Long id, Pageable pageable) {
        return articleRepository.findArticleByTagIdAndPublished(id, pageable);
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
