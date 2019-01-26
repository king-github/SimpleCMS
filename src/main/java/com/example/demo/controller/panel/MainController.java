package com.example.demo.controller.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("panel")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping()
    public String index(Model model, Principal principal, Authentication authentication) {

        logger.info("Panel");

        model.addAttribute("title", "Simple CMS");
        return "panel/index";
    }
}
