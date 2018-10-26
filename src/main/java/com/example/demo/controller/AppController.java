package com.example.demo.controller;

import com.example.demo.error.NotFoundException;
import com.example.demo.services.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/")
    public String home (Model model){


        logger.info("Home page");

        String[] items = {"First", "Second", "Third"};

        model.addAttribute(new Date());
        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-mm-dd"));

        return "default/index";
    }

    @GetMapping(value = "/article/{id}")
    public String showArticle (@PathVariable Long id, Model model){

        String[] items = {"First", "Second", "Third"};

        logger.info("Get article id={}", id);

        model.addAttribute(new Date());
        model.addAttribute("items", items);
        model.addAttribute("article", articleService.findArticle(id));
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-mm-dd"));

        return "default/article";
    }

}
