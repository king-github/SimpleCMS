package com.example.demo.repository;


import com.example.demo.entity.Article;
import com.example.demo.form.panel.ArticleSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ArticleRepositoryCustom {


    Page<Article> findArticlesByExample(Article article, Pageable pageable);

    Page<Article> findArticlesByCriteria(ArticleSearchForm articleSearchForm, Pageable pageable);

}
