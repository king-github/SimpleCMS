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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>,
                                           ArticleRepositoryCustom,
                                           JpaSpecificationExecutor<Article> {

    @EntityGraph(attributePaths = { "author", "section" })
    <S extends Article> Page<S> findAll(Example<S> example, Pageable pageable);

    @EntityGraph(attributePaths = { "author", "section" })
    Page<Article> findAll(@Nullable Specification<Article> var1, Pageable var2);

    Page<Article> findArticleByPublishedTrue(Pageable pageable);

    Page<Article> findArticleByAuthorId(Long id, Pageable pageable);

    Page<Article> findArticleByAuthorIdAndPublishedTrue(Long id, Pageable pageable);

    Page<Article> findArticleBySectionIdAndPublishedTrue(Long id, Pageable pageable);

    @Query("select a from Article a join a.tags t where t.id = ?1 and a.published=TRUE")
    Page<Article> findArticleByTagIdAndPublished(Long id, Pageable pageable);
}
