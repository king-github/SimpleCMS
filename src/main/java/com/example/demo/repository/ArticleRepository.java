package com.example.demo.repository;


import com.example.demo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findArticleByAuthorId(Long id);

//    SELECT *
//    FROM ARTICLE_TAG AT
//    LEFT JOIN ARTICLE A
//    ON A.id=AT.ARTICLE_ID
//    WHERE TAG_ID = ?

    @Query("select a from Article a join a.tags t where t.id = ?1")
    List<Article> findArticleByTagId(Long id);
}
