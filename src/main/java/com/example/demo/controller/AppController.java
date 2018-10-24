package com.example.demo.controller;

import com.example.demo.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class AppController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/")
    String home (Model model){

        String[] items = {"First", "Second", "Third"};

        model.addAttribute(new Date());
        model.addAttribute("items", items);
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-mm-dd"));

        System.out.println("Date: "+ new Date());

        return "default/index";
    }

    @GetMapping(value = "/article/{id}")
    String showArticle (@PathVariable Long id, Model model){

        String[] items = {"First", "Second", "Third"};

        System.out.println("ID: "+ id);

        model.addAttribute(new Date());
        model.addAttribute("items", items);
        model.addAttribute("article", articleService.findArticle(id));
        model.addAttribute("title", "Home Page");
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-mm-dd"));



        return "default/article";
    }
}
