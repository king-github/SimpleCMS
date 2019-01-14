package com.example.demo.integration;

import com.example.demo.entity.Article;
import com.example.demo.entity.Author;
import com.example.demo.entity.Section;
import com.example.demo.entity.User;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.repository.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@Transactional
public class ArticlesFilterTest {

	@Autowired
	private ArticleRepository repository;

	private PageRequest pageRequest;

	private Author alan;
	private Author ben;
	private Author bill;

	private Section lifestyle;
	private Section games;

	private Article article1;
	private Article article2;
	private Article article3;

	@Before
	public void init() {

		pageRequest = PageRequest.of(0, 1000);

		lifestyle = new Section("Lifestyle");
		games = new Section("Games");

		alan = new Author("user1", "user1@mail.com", User.UserStatus.ACTIVE,
				"xxx", "Alan", "Alfa");
		ben = new Author("user2", "user2@mail.com", User.UserStatus.ACTIVE,
				"xxx","Ben", "Bond");
		bill = new Author("user3", "user3@mail.com", User.UserStatus.ACTIVE,
				"xxx","Bill", "Bond");

		article1=new Article("Title 1", "Lead 1", "Content 1", alan);
		article1.setSection(lifestyle);
		article1.setPublicationDate(LocalDateTime.of(2018, 5, 10, 0, 0));
		article1.setPublished(true);

		article2=new Article("Title 2", "Lead 2", "Content 2", ben);
		article2.setSection(lifestyle);
		article2.setPublicationDate(LocalDateTime.of(2018, 5, 11, 12, 0));
		article2.setPublished(true);

		article3=new Article("Title 3", "Lead 3", "Content 3", bill);
		article3.setSection(games);
		article3.setPublicationDate(LocalDateTime.of(2018, 5, 12, 0, 0));
		article3.setPublished(false);

		repository.save(article1);
		repository.save(article2);
		repository.save(article3);
	}

	@Test
	public void giveNoFilterThenReceiveAllArticles () {

		List<Article> results = repository.findAll();

		assertThat(article1, isIn(results));
		assertThat(article2, isIn(results));
		assertThat(article3, isIn(results));
		assertEquals(3, results.size());
	}

	@Test
	public void giveTitleThenReceiveArticlesContainTitle () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setTitle("1");

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, isIn(results));
		assertThat(article2, not(isIn(results)));
		assertThat(article3, not(isIn(results)));
		assertEquals(1, results.size());
	}

	@Test
	public void giveFirstnameThenReceiveArticlesContainFirstnamee () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setFirstname("B");

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, not(isIn(results)));
		assertThat(article2, isIn(results));
		assertThat(article3, isIn(results));
		assertEquals(2, results.size());
	}

	@Test
	public void giveLastnameThenReceiveArticlesContainLastname () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setLastname("Bond");

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, not(isIn(results)));
		assertThat(article2, isIn(results));
		assertThat(article3, isIn(results));
		assertEquals(2, results.size());
	}

	@Test
	public void giveSectionIdThenReceiveArticlesHasThatSection () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setSectionId(games.getId());

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, not(isIn(results)));
		assertThat(article2, not(isIn(results)));
		assertThat(article3, isIn(results));
		assertEquals(1, results.size());
	}

	@Test
	public void giveDateFromThenReceiveArticlesAfterOrEqualPublicationDate () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setDateFrom(LocalDate.of(2018, 5, 11));

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, not(isIn(results)));
		assertThat(article2, isIn(results));
		assertThat(article3, isIn(results));
		assertEquals(2, results.size());
	}

	@Test
	public void giveDateToThenReceiveArticlesBeforeOrEqualPublicationDate () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setDateTo(LocalDate.of(2018, 5, 11));

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, isIn(results));
		assertThat(article2, isIn(results));
		assertThat(article3, not(isIn(results)));
		assertEquals(2, results.size());
	}

	@Test
	public void givePublishedThenReceiveArticlesPublished () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setPublished(true);

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, isIn(results));
		assertThat(article2, isIn(results));
		assertThat(article3, not(isIn(results)));
		assertEquals(2, results.size());
	}

	@Test
	public void giveFullSearchFormThenReceiveArticles () {

		ArticleSearchForm articleSearchForm = new ArticleSearchForm();
		articleSearchForm.setTitle("2");
		articleSearchForm.setFirstname(ben.getFirstname().toUpperCase());
		articleSearchForm.setLastname(ben.getLastname().toLowerCase());
		articleSearchForm.setDateFrom(LocalDate.of(2018, 5, 11));
		articleSearchForm.setDateTo(LocalDate.of(2018, 5, 11));
		articleSearchForm.setPublished(true);
		articleSearchForm.setSectionId(lifestyle.getId());

		List<Article> results = repository.findArticlesByCriteria(articleSearchForm, pageRequest).getContent();

		assertThat(article1, not(isIn(results)));
		assertThat(article2, isIn(results));
		assertThat(article3, not(isIn(results)));
		assertEquals(1, results.size());
	}
}