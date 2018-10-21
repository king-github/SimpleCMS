package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Date;

@Controller
public class AppController {

    @GetMapping(value = "/")
    String home (Model model){

        String[] items = {"First", "Second", "Third"};

        model.addAttribute(new Date());
        model.addAttribute("items", items);

        System.out.println("Date: "+ new Date());

        return "index";
    }

}
