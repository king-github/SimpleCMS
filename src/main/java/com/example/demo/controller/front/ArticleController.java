package com.example.demo.controller.front;

import com.example.demo.entity.Article;

import com.example.demo.entity.Tag;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PageSizeHelper;
import com.example.demo.helper.PagerParamsHelper;

import com.example.demo.services.ArticleService;
import com.example.demo.services.AuthorService;
import com.example.demo.services.SectionService;
import com.example.demo.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;


@Controller
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final int ARTICLES_PER_PAGE = 5;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    @Qualifier("articleFrontOrderModeHelper")
    private OrderModeHelper sortModeHelper;

    @Autowired
    private PageSizeHelper pageSizeHelper;


    @GetMapping(value = "/")
    public String home (Model model,
                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
                        ){

        logger.info("Home page");

        model.addAttribute("articles", articleService.getAllPublishedArticles( pageable ));
        model.addAttribute("title", "Home Page");
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        return "front/index";
    }

    @GetMapping(value = "/article/{id}")
    public String showArticle (@PathVariable Long id, Model model){

        logger.info("Get article id={}", id);

        Article article = articleService.findArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("title", article.getTitle());

        return "front/article";
    }

    @GetMapping(value = "/author/{id}")
    public String showArticleByAuthor (@PathVariable Long id,
                                       Model model,
                                       @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
                                       ){

        logger.info("Home page");

        model.addAttribute("articles", articleService.findPublishedArticleByAuthor(id, pageable ));
        model.addAttribute("title", "Home Page");
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        return "front/index";
    }

    @GetMapping(value = "/section/{id}")
    public String showArticleBySection (@PathVariable Long id,
                                        Model model,
                                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
    ){

        logger.info("Get articles from section id = {}", id);

        model.addAttribute("articles", articleService.findPublishedArticleBySection(id, pageable ));
        model.addAttribute("title", "Home Page");
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        return "front/index";
    }

    @GetMapping(value = "/tag/{id:\\d+}")
    public String showArticleByTagId (@PathVariable Long id, Model model,
                                      @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
                                      ){

        logger.info("Tag Page - id: "+id);

        Tag tag = tagService.findTagById(id);
        model.addAttribute("articles", articleService.findPublishedArticleByTag(tag.getId(), pageable ));
        model.addAttribute("title", "Tag: "+tag.getName());
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        return "front/index";
    }

    @GetMapping(value = "/tag/{name:[A-Za-z]\\w+}")
    public String showArticleByTagName (@PathVariable String name,
                                        Model model,
                                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
                                       ){

        logger.info("Tag Page - name: "+name);

        Tag tag = tagService.findTagByName(name);
        model.addAttribute("articles", articleService.findPublishedArticleByTag(tag.getId(), pageable ));
        model.addAttribute("title", "Tag: "+tag.getName());
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        return "front/index";
    }

    @ModelAttribute
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("tags", tagService.getAllTagsWithQuantity());
        model.addAttribute("authors", authorService.getAllAuthorsWithQuantity());
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }

}
