package com.example.demo.integration;

import com.example.demo.entity.*;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.SectionRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.services.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class ArticlesRepositoryTest {

	@Autowired
	private ArticleRepository repository;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private TagRepository tagRepository;

	private PageRequest pageRequest;

	private Author alan;
	private Author ben;
	private Author bill;

	private Section lifestyle;
	private Section games;

	private Article article1;
	private Article article2;
	private Article article3;

	private Tag tag1;
	private Tag tag2;
	private Tag tag3;

	@Before
	public void init() {

		lifestyle = new Section("Lifestyle");
		games = new Section("Games");

		alan = new Author("user1", "user1@mail.com", User.UserStatus.ACTIVE,
				"xxx", "Alan", "Alfa");
		ben = new Author("user2", "user2@mail.com", User.UserStatus.ACTIVE,
				"xxx","Ben", "Bond");
		bill = new Author("user3", "user3@mail.com", User.UserStatus.ACTIVE,
				"xxx","Bill", "Bond");

		tag1 = new Tag("tag1");
		tag2 = new Tag("tag2");
		tag3 = new Tag("tag3");

		article1=new Article("Title 1", "Lead 1", "Content 1", alan);
		article1.setSection(lifestyle);
		article1.setPublicationDate(LocalDateTime.of(2018, 5, 10, 0, 0));
		article1.setPublished(true);
		article1.getTags().addAll(Arrays.asList(tag1, tag2, tag3));

		article2=new Article("Title 2", "Lead 2", "Content 2", ben);
		article2.setSection(lifestyle);
		article2.setPublicationDate(LocalDateTime.of(2018, 5, 11, 12, 0));
		article2.setPublished(true);
		article2.getTags().addAll(Arrays.asList(tag2, tag3));

		article3=new Article("Title 3", "Lead 3", "Content 3", bill);
		article3.setSection(games);
		article3.setPublicationDate(LocalDateTime.of(2018, 5, 12, 0, 0));
		article3.setPublished(false);
		article3.getTags().addAll(Arrays.asList(tag3));

		repository.save(article1);
		repository.save(article2);
		repository.save(article3);
	}

	@Test
	public void giveAllIdsThenDeleteAllArticles () {

		int removedCount = articleService.deleteArticles(
		        Arrays.asList(article1.getId(), article2.getId(), article3.getId()));
		List<Article> results = repository.findAll();
		List<Author> authors = authorRepository.findAll();
		List<Section> sections = sectionRepository.findAll();

		assertEquals(3, removedCount);
		assertEquals(0, results.size());
		assertEquals(2, sections.size());
		assertEquals(3, authors.size());

		assertEquals(0, tagRepository.countArticlesWithTagId(tag1.getId()).intValue());
		assertEquals(0, tagRepository.countArticlesWithTagId(tag2.getId()).intValue());
		assertEquals(0, tagRepository.countArticlesWithTagId(tag3.getId()).intValue());
	}

	@Test
	public void removeOnlyArticlesWithIds () {

        long removedCount = articleService.deleteArticles(Arrays.asList(article1.getId(), 90L, 91L, 92L));
		List<Article> results = repository.findAll();

		assertEquals(1L, removedCount);
		assertEquals(2, results.size());

		assertEquals(0L, tagRepository.countArticlesWithTagId(tag1.getId()).longValue());
		assertEquals(1L, tagRepository.countArticlesWithTagId(tag2.getId()).longValue());
		assertEquals(2L, tagRepository.countArticlesWithTagId(tag3.getId()).longValue());
	}

	@Test
	public void givenTagId_whenFindAllArticlesByTagId_thenGetArticlesWithCorrectTagsSets(){

		List<Article> results = repository.findAllArticlesByTagId(tag3.getId());

		assertEquals(3L, results.size());
		assertThat(results, containsInAnyOrder(article1, article2, article3));

        assertThat(results, hasItems(allOf(hasProperty("id", equalTo(article1.getId())),
                                           hasProperty("tags", containsInAnyOrder(tag1, tag2, tag3))),
                                     allOf(hasProperty("id", equalTo(article2.getId())),
                                           hasProperty("tags", containsInAnyOrder(tag2, tag3))),
                                     allOf(hasProperty("id", equalTo(article3.getId())),
                                           hasProperty("tags", containsInAnyOrder(tag3)))
                            )
        );
	}

    @Test
    public void givenTagId_whenFindAllArticlesByTagId_thenGetPublishedArticlesWithCorrectTagsSets(){

        Page<Article> page = repository.findArticleByTagIdAndPublished(tag3.getId(),
                PageRequest.of(0, 10));

		List<Article> results = page.getContent();

		assertEquals(2L, page.getTotalElements());
		assertEquals(2L, results.size());
        assertThat(results, containsInAnyOrder(article1, article2));
        assertThat(results, hasItems(allOf(hasProperty("id", equalTo(article1.getId())),
                                           hasProperty("tags", hasSize(3)),
                                           hasProperty("tags", containsInAnyOrder(tag1, tag2, tag3))),
                                     allOf(hasProperty("id", equalTo(article2.getId())),
                                           hasProperty("tags", hasSize(2)),
                                           hasProperty("tags", containsInAnyOrder(tag2, tag3)))
                )
        );
    }

	@Test
	public void givenTagId_whenFindAllArticlesByTagId_thenGetPublishedArticlesWithCorrectTotalElements(){

		Page<Article> page = repository.findArticleByTagIdAndPublished(tag3.getId(),
				PageRequest.of(0, 1));

		assertEquals(2L, page.getTotalElements());

	}

}