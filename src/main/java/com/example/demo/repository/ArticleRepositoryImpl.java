package com.example.demo.repository;


import com.example.demo.entity.Article;
import com.example.demo.entity.Author;
import com.example.demo.entity.Section;
import com.example.demo.form.panel.ArticleSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Transactional(readOnly = true)
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> findArticlesByExample(Article article, Pageable pageable) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("published")
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.endsWith())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Article> example = Example.of(article, matcher);

        return articleRepository.findAll(example, pageable);
    }

    static Specification<Article> titleContains(String title) {
        return (book, cq, cb) -> cb.like(book.get("title"), "%" + title + "%");
    }

    @Override
    public Page<Article> findArticlesByCriteria(ArticleSearchForm articleSearchForm, Pageable pageable) {


            Specification<Article> where = Specification.where((root, criteriaQuery, criteriaBuilder) -> {

                final Join<Article, Author> authors = root.join("author", JoinType.LEFT);
                final Join<Article, Section> sections = root.join("section", JoinType.LEFT);

                List<Predicate> predicateList = new ArrayList<>();

                if (articleSearchForm.getTitle() != null && !articleSearchForm.getTitle().isEmpty())
                   predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                           "%"+articleSearchForm.getTitle().toLowerCase()+"%"));

                if (articleSearchForm.getFirstname() != null && !articleSearchForm.getFirstname().isEmpty())
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(authors.get("firstname")),
                           "%"+articleSearchForm.getFirstname().toLowerCase()+"%"));

                if (articleSearchForm.getLastname() != null && !articleSearchForm.getLastname().isEmpty())
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(authors.get("lastname")),
                           "%"+articleSearchForm.getLastname().toLowerCase()+"%"));

                if (articleSearchForm.getSectionId() != null)
                    predicateList.add(criteriaBuilder.equal(sections.get("id"),
                            articleSearchForm.getSectionId()));

                if (articleSearchForm.getPublished() != null)
                    predicateList.add(criteriaBuilder.equal(root.get("published"),
                            articleSearchForm.getPublished()));

                if (articleSearchForm.getDateFrom() != null) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"),
                            LocalDateTime.of(articleSearchForm.getDateFrom(),
                                    LocalTime.of(0, 0))));
                }
                if (articleSearchForm.getDateTo() != null) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"),
                            LocalDateTime.of(articleSearchForm.getDateTo(),
                                    LocalTime.of(23,59,59,999999999))));
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            });

            return articleRepository.findAll(where, pageable);
    }
}
