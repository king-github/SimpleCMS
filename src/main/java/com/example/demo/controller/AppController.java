package com.example.demo.controller;

import com.example.demo.entity.Article;

import com.example.demo.entity.Tag;
import com.example.demo.services.ArticleService;
import com.example.demo.services.AuthorService;
import com.example.demo.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;


@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AuthorService authorService;

    final static private int ARTICLES_PER_PAGE = 5;

    @GetMapping(value = "/")
    public String home (Model model,
                        @PageableDefault(page = 0, size = ARTICLES_PER_PAGE) Pageable pageable
                        ){


        logger.info("Home page");

        String[] items = {"First", "Second", "Third"};

        model.addAttribute("items", items);
        addDefaultAttributeToModel(model);
        model.addAttribute("articles", articleService.getAllArticles( pageable ));
        model.addAttribute("title", "Home Page");

        return "default/index";
    }

    @GetMapping(value = "/article/{id}")
    public String showArticle (@PathVariable Long id, Model model){

        logger.info("Get article id={}", id);
        Article article = articleService.findArticle(id);

        addDefaultAttributeToModel(model);
        model.addAttribute("article", article);
        model.addAttribute("title", article.getTitle());

        return "default/article";
    }

    @GetMapping(value = "/author/{id}")
    public String showArticleByAuthor (@PathVariable Long id,
                                       Model model,
                                       @PageableDefault(page = 0, size = ARTICLES_PER_PAGE) Pageable pageable
                                       ){

        logger.info("Home page");

        String[] items = {"First", "Second", "Third"};

        model.addAttribute("items", items);
        addDefaultAttributeToModel(model);
        model.addAttribute("articles", articleService.findArticleByAuthor(id, pageable ));
        model.addAttribute("title", "Home Page");

        return "default/index";
    }

    @GetMapping(value = "/tag/{id:\\d+}")
    public String showArticleByTagId (@PathVariable Long id, Model model,
                                      @PageableDefault(page = 0, size = ARTICLES_PER_PAGE) Pageable pageable
                                      ){

        logger.info("Tag Page - id: "+id);

        String[] items = {"First", "Second", "Third"};

        Tag tag = tagService.findTagById(id);

        model.addAttribute("items", items);
        addDefaultAttributeToModel(model);
        model.addAttribute("articles", articleService.findArticleByTag(tag.getId(), pageable ));
        model.addAttribute("title", "Tag: "+tag.getName());

        return "default/index";
    }

    @GetMapping(value = "/tag/{name:[A-Za-z]\\w+}")
    public String showArticleByTagName (@PathVariable String name,
                                        Model model,
                                        @PageableDefault(page = 0, size = ARTICLES_PER_PAGE) Pageable pageable
                                       ){

        logger.info("Tag Page - name: "+name);

        String[] items = {"First", "Second", "Third"};

        Tag tag = tagService.findTagByName(name);

        model.addAttribute("items", items);
        addDefaultAttributeToModel(model);
        model.addAttribute("articles", articleService.findArticleByTag(tag.getId(), pageable ));
        model.addAttribute("title", "Tag: "+tag.getName());

        return "default/index";
    }

    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("tags", tagService.getAllTagsWithQuantity());
        model.addAttribute("authors", authorService.getAllAuthorsWithQuantity());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
