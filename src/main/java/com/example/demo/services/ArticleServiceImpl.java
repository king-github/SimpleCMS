package com.example.demo.services;

import com.example.demo.entity.Article;

import com.example.demo.error.NotFoundException;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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
    public Page<Article> getAllArticles(ArticleSearchForm articleSearchForm, Pageable pageable) {
        return articleRepository.findArticlesByCriteria(articleSearchForm, pageable);
    }

    @Override
    public Page<Article> getAllArticles(Example<Article> example, Pageable pageable) {
        return articleRepository.findAll(example, pageable);
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
    public Page<Article> findPublishedArticleBySection(Long id, Pageable pageable) {
        return articleRepository.findArticleBySectionIdAndPublishedTrue(id, pageable);
    }

    @Override
    public Page<Article> findArticlesByExample(Article article, Pageable pageable) {
        return articleRepository.findArticlesByExample(article, pageable);
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

    @Override
    public Optional<Article> findArticleById(Long id) {

        if (id == null) return Optional.empty();
        return articleRepository.findById(id);
    }


    @Transactional
    @Override
    public int deleteArticles(Iterable<Long> ids) {

        List<Article> allById = articleRepository.findAllById(ids);
        articleRepository.deleteInBatch(allById);
        return allById.size();
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }


}
