package com.example.demo.integration;

import com.example.demo.entity.Article;
import com.example.demo.entity.Author;
import com.example.demo.entity.Section;
import com.example.demo.entity.Tag;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ArticlesDeleteTest {

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

		alan = new Author("Alan", "Alfa");
		ben = new Author("Ben", "Bond");
		bill = new Author("Bill", "Bond");

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

		int removedCount = articleService.deleteArticles(Arrays.asList(article1.getId(), article2.getId(), article3.getId()));
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

		int removedCount = articleService.deleteArticles(Arrays.asList(article1.getId(), 90L, 92L, 1099L));
		List<Article> results = repository.findAll();

		assertEquals(1, removedCount);
		assertEquals(2, results.size());

		assertEquals(0, tagRepository.countArticlesWithTagId(tag1.getId()).intValue());
		assertEquals(1, tagRepository.countArticlesWithTagId(tag2.getId()).intValue());
		assertEquals(2, tagRepository.countArticlesWithTagId(tag3.getId()).intValue());
	}


}