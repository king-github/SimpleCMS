package com.example.demo;

import com.example.demo.entity.Author;
import com.example.demo.entity.Section;
import com.example.demo.entity.Tag;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("dev")
public class FixturesDev {

    private final int NUM_OF_ARTICLES = 55;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TagRepository tagRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {


        Random random = new Random();

        Stream.of("movies", "books", "music", "people", "java8")
              .forEach(s -> tagRepository.save(new Tag(s)));

        Section[] sections = { new Section("Programing"), new Section("Games"),
                               new Section("Lifestyle"), new Section("Funny"),
                               new Section("Travels"), new Section("Hardware") };

        Author[] authors = { new Author("Jony", "Pewnon"), new Author("Andy", "Brandy"), new Author("Phil", "Bill"),
                             new Author("Abdul", "Gewnon"), new Author("Andrew", "Fandy"), new Author("Elisa", "Bill"),
                             new Author("Sam", "Dewnon"), new Author("Jude", "Crandy"), new Author("Mike", "Bill"),
                             new Author("John", "Salami"), new Author("Agnes", "Frindy"), new Author("Sylvana", "Athon")
                           };

        for (int i=1; i<= NUM_OF_ARTICLES; i++) {

            Article article = new Article("Article title nr: " + i,
                                          "Lorem ipsum czxczx... " + i,
                                          "Content Lorem ipsuma dsnfsd fsdn fdf",
                                          authors[random.nextInt(authors.length)]);

            article.setSection(sections[random.nextInt(sections.length)]);
            article.setPublished(random.nextInt(100) > 10);
            article.setPublicationDate(ZonedDateTime.now()
                                                    .toLocalDateTime()
                                                    .minusMinutes(random.nextInt(60*24*365)));
            List<Tag> tags = tagRepository.findAll();

            article.setTags( tags.stream()
                                 .filter(tag -> random.nextBoolean())
                                 .collect(Collectors.toSet()));

            articleRepository.save(article);
        }

        authorRepository.save(new Author("John", "Nothing"));
        authorRepository.save(new Author("Barry", "Lazy"));
        tagRepository.save(new Tag("Empty"));
        tagRepository.save(new Tag("Null"));
    }
}
