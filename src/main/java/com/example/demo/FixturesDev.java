package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile({"dev", "dev2"})
public class FixturesDev {

    private final int NUM_OF_ARTICLES = 55;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SectionRepository sectionRepository;

    private Role articleEditor;
    private Role tagEditor;
    private Role sectionEditor;

    private Author builAuthor(String username, String firstname, String lastname) {

        return new Author(username, username+"@mail.com",
                User.UserStatus.ACTIVE, "xxx",
                firstname, lastname);
    }

    @Transactional
    private void saveRoles(){

        articleEditor = new Role("ROLE_ARTICLE_EDITOR", "article editor",
                new Privilege("SHOW_ARTICLES", "show"),
                new Privilege("EDIT_ARTICLES", "edit"),
                new Privilege("DELETE_ARTICLES", "delete"));

        tagEditor = new Role("ROLE_TAG_EDITOR", "tag editor",
                new Privilege("SHOW_TAGS", "show"),
                new Privilege("EDIT_TAGS", "edit"),
                new Privilege("DELETE_TAGS", "delete"));

        sectionEditor = new Role("ROLE_SECTION_EDITOR", "section editor",
                new Privilege("SHOW_SECTIONS", "show"),
                new Privilege("EDIT_SECTIONS", "edit"),
                new Privilege("DELETE_SECTIONS", "delete"));

        roleRepository.saveAll(Arrays.asList(articleEditor, tagEditor, sectionEditor));

    }

    @Transactional
    private void saveUsers(){

        User user1 = new User("user1", "user1@mail.com", User.UserStatus.ACTIVE, "xxx");
        User user2 = new User("user2", "user2@mail.com", User.UserStatus.NEW, "xxx");
        User user3 = new User("user3", "user3@mail.com", User.UserStatus.INACTIVE, "xxx");

        user1.addRoles(articleEditor, tagEditor, sectionEditor);
        user2.addRoles(articleEditor, tagEditor);
        user3.addRoles(articleEditor);

        List<User> users = Arrays.asList(user1, user2, user3);
        userRepository.saveAll(users);
    }


    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Random random = new Random();

        saveRoles();
        saveUsers();

        Stream.of("movies", "books", "music", "people", "java8")
              .forEach(s -> tagRepository.save(new Tag(s)));

        Section[] sections={ new Section("Programing"), new Section("Games"),
                             new Section("Lifestyle"), new Section("Funny"),
                             new Section("Travels"), new Section("Hardware")
        };

        Author[] authors= {
                builAuthor("editor1", "John","Salami"),
                builAuthor("editor2", "Andrew","Fandy"),
                builAuthor("editor3", "Elisa","Bill"),
                builAuthor("editor4", "Jude","Crandy"),
                builAuthor("editor5", "Mike","Bill"),
                builAuthor("editor6", "Abdul","Gewnon"),
                builAuthor("editor7", "Agnes","Frindy"),
                builAuthor("editor8", "Jony","Pewnon"),
                builAuthor("editor9", "Sam","Dewnon"),
                builAuthor("editor10", "Sylvana","Athon"),
                builAuthor("editor11", "Andy","Brandy"),
                builAuthor("editor12", "Phil","Bill")
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

            article.setTags(tags.stream()
                                .filter(tag -> random.nextBoolean())
                                .collect(Collectors.toSet()));

            articleRepository.save(article);
        }

        sectionRepository.saveAll(Arrays.asList(sections));

        tagRepository.saveAll(Arrays.asList(new Tag("Empty"), new Tag("Null")));
    }
}
