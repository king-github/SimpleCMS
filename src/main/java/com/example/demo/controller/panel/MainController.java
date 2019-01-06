package com.example.demo.controller.panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("panel")
public class MainController {

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("title", "Simple CMS");
        return "panel/index";
    }
}
