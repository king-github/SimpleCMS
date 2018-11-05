package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.ArticleService;
import com.example.demo.entity.Tag;
import com.example.demo.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/")
    public String home (Model model){


        logger.info("Home page");

        String[] items = {"First", "Second", "Third"};

        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return "default/index";
    }

    @GetMapping(value = "/article/{id}")
    public String showArticle (@PathVariable Long id, Model model){

        String[] items = {"First", "Second", "Third"};

        logger.info("Get article id={}", id);
        Article article = articleService.findArticle(id);

        model.addAttribute(new Date());
        model.addAttribute("items", items);
        model.addAttribute("article", article);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return "default/article";
    }

    @GetMapping(value = "/author/{id}")
    public String showArticleByAuthor (@PathVariable Long id, Model model){

        logger.info("Home page");

        String[] items = {"First", "Second", "Third"};

        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.findArticleByAuthor(id));
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return "default/index";
    }

    @GetMapping(value = "/tag/{id:\\d+}")
    public String showArticleByTagId (@PathVariable Long id, Model model){

        logger.info("Tag Page - id: "+id);

        String[] items = {"First", "Second", "Third"};

        Tag tag = tagService.findTagById(id);

        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.findArticleByTag(tag.getId()));
        model.addAttribute("title", "Tag: "+tag.getName());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return "default/index";
    }

    @GetMapping(value = "/tag/{name:[A-Za-z]\\w+}")
    public String showArticleByTagName (@PathVariable String name, Model model){

        logger.info("Tag Page - name: "+name);

        String[] items = {"First", "Second", "Third"};

        Tag tag = tagService.findTagByName(name);

        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.findArticleByTag(tag.getId()));
        model.addAttribute("title", "Tag: "+tag.getName());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return "default/index";
    }



}
