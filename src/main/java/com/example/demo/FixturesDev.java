package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Role articleEditorRole;
    private Role tagEditorRole;
    private Role sectionEditorRole;
    private Role adminRole;
    private Role viewerRole;

    private Author buildAuthor(String username, String firstname, String lastname) {

        return new Author(username,
                    username+"@mail.com",
                          User.UserStatus.ACTIVE,
                          passwordEncoder.encode("pass"),
                          firstname,
                          lastname);
    }

    @Transactional
    private void savePrivileges(){

        privilegeRepository.saveAll(Arrays.asList(
                new Privilege("SHOW_ARTICLES", "show"),
                new Privilege("EDIT_ARTICLES", "edit"),
                new Privilege("DELETE_ARTICLES", "delete"),

                new Privilege("SHOW_TAGS", "show"),
                new Privilege("EDIT_TAGS", "edit"),
                new Privilege("DELETE_TAGS", "delete"),

                new Privilege("SHOW_SECTIONS", "show"),
                new Privilege("EDIT_SECTIONS", "edit"),
                new Privilege("DELETE_SECTIONS", "delete"),

                new Privilege("SHOW_USERS", "show"),
                new Privilege("EDIT_USERS", "edit"),
                new Privilege("DELETE_USERS", "delete")
        ));

    }

    @Transactional
    private void saveRoles(){

        articleEditorRole = new Role("ROLE_ARTICLE_EDITOR", "article editor",
                privilegeRepository.findByNameIn("SHOW_ARTICLES", "EDIT_ARTICLES", "DELETE_ARTICLES"));


        tagEditorRole = new Role("ROLE_TAG_EDITOR", "tag editor",
                privilegeRepository.findByNameIn("SHOW_TAGS", "EDIT_TAGS", "DELETE_TAGS"));

        sectionEditorRole = new Role("ROLE_SECTION_EDITOR", "section editor",
                privilegeRepository.findByNameIn("SHOW_SECTIONS", "EDIT_SECTIONS", "DELETE_SECTIONS"));

        viewerRole = new Role("ROLE_VIEWER", "viewer",
                privilegeRepository.findByNameIn("SHOW_USERS", "SHOW_ARTICLES", "SHOW_TAGS", "SHOW_SECTIONS"));

        adminRole = new Role("ROLE_ADMIN", "administrator",
                privilegeRepository.findAll());


        roleRepository.saveAll(Arrays.asList(articleEditorRole, tagEditorRole, sectionEditorRole, viewerRole));

    }

    @Transactional
    private void saveUsers(){

        Author viewer1 = new Author("viewer", "viewer@mail.com", User.UserStatus.ACTIVE,
                passwordEncoder.encode("pass"), "See","You");
        viewer1.addRoles(Arrays.asList(viewerRole));

        Author admin1 = new Author("admin", "admin@mail.com", User.UserStatus.ACTIVE,
                passwordEncoder.encode("pass"), "Linus","Minus");
        admin1.addRoles(Arrays.asList(adminRole));

        User user1 = new User("user1", "user1@mail.com",
                User.UserStatus.ACTIVE, passwordEncoder.encode("pass1"));
        User user2 = new User("user2", "user2@mail.com",
                User.UserStatus.NEW, passwordEncoder.encode("pass2"));
        User user3 = new User("user3", "user3@mail.com",
                User.UserStatus.INACTIVE, passwordEncoder.encode("pass3"));

        user1.addRoles(Arrays.asList(articleEditorRole, tagEditorRole, sectionEditorRole));
        user2.addRoles(Arrays.asList(articleEditorRole, tagEditorRole));
        user3.addRoles(Arrays.asList(articleEditorRole));

        List<User> users = Arrays.asList(user1, user2, user3, admin1, viewer1);
        userRepository.saveAll(users);
    }


    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Random random = new Random();

        savePrivileges();
        saveRoles();
        saveUsers();

        Stream.of("movies", "books", "music", "people", "java8")
              .forEach(s -> tagRepository.save(new Tag(s)));

        Section[] sections={ new Section("Programing"), new Section("Games"),
                             new Section("Lifestyle"), new Section("Funny"),
                             new Section("Travels"), new Section("Hardware")
        };

        Author[] authors= {
                buildAuthor("editor1", "John","Salami"),
                buildAuthor("editor2", "Andrew","Fandy"),
                buildAuthor("editor3", "Elisa","Bill"),
                buildAuthor("editor4", "Jude","Crandy"),
                buildAuthor("editor5", "Mike","Bill"),
                buildAuthor("editor6", "Abdul","Gewnon"),
                buildAuthor("editor7", "Agnes","Frindy"),
                buildAuthor("editor8", "Jony","Pewnon"),
                buildAuthor("editor9", "Sam","Dewnon"),
                buildAuthor("editor10", "Sylvana","Athon"),
                buildAuthor("editor11", "Andy","Brandy"),
                buildAuthor("editor12", "Phil","Bill")
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
        sectionRepository.save(new Section("Empty section"));
        tagRepository.saveAll(Arrays.asList(new Tag("Empty"), new Tag("Null")));
    }
}
