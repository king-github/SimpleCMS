package com.example.demo.repository;


import com.example.demo.entity.Article;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>,
                                           ArticleRepositoryCustom,
                                           JpaSpecificationExecutor<Article> {

    //@EntityGraph(attributePaths = { "author", "section" })
    @EntityGraph(value="Article.allFetch")
    Page<Article> findAll(@Nullable Specification<Article> var1, Pageable var2);

    //@EntityGraph(attributePaths = { "author", "section", "tags" })
    @EntityGraph(value="Article.allFetch")
    <S extends Article> Page<S> findAll(Example<S> example, Pageable pageable);

    @EntityGraph(value="Article.allFetch")
    Page<Article> findArticleByPublishedTrue(Pageable pageable);

    @EntityGraph(value="Article.allFetch")
    Page<Article> findArticleByAuthorId(Long id, Pageable pageable);

    @EntityGraph(value="Article.allFetch")
    Page<Article> findArticleByAuthorIdAndPublishedTrue(Long id, Pageable pageable);

    @EntityGraph(value="Article.allFetch")
    Page<Article> findArticleBySectionIdAndPublishedTrue(Long id, Pageable pageable);


    @Query(value = "select a " +
                   "from Article a " +
                   "join a.tags t " +
                   "join fetch a.tags " +
                   "join fetch a.section " +
                   "join fetch a.author " +
                   "where t.id= :id and a.published=TRUE",
    countQuery = "select COUNT(a) from Article a join a.tags t where t.id= :id and a.published=TRUE")
    Page<Article> findArticleByTagIdAndPublished(@Param("id") Long id, Pageable pageable);

    //@EntityGraph(value="Article.allFetch", type = LOAD)
    @Query(value = "select distinct a " +  // distinct needed for h2 patch problem
           "from Article a " +
           "join a.tags t " +
           "join fetch a.tags " +
           "join fetch a.section " +
           "join fetch a.author " +
           "where t.id= ?1 ",
            countQuery = "select COUNT(a) from Article a join a.tags t where t.id= :id")
    List<Article> findAllArticlesByTagId(Long id);
}
