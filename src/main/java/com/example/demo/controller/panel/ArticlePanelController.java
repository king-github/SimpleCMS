package com.example.demo.controller.panel;

import com.example.demo.controller.front.ArticleController;
import com.example.demo.helper.PageSizeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.helper.SortModeHelper;
import com.example.demo.services.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping("panel")
public class ArticlePanelController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private static final int ARTICLES_PER_PAGE = 10;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SortModeHelper sortModeHelper;

    @Autowired
    private PageSizeHelper pageSizeHelper;

    @Autowired
    private PagerParamsHelper pagerParamsHelper;

    @GetMapping(value = "")
    public String home (Model model,
                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable
                        ){

        logger.info("Articles");

        model.addAttribute("articles", articleService.getAllArticles( pageable ));
        model.addAttribute("title", "Article list");

        return "panel/index";
    }


    @ModelAttribute
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("pagerParamsHelper", pagerParamsHelper);
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }
}
