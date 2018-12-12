package com.example.demo.controller.panel;

import com.example.demo.controller.front.ArticleController;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.form.panel.ArticleSearchFormMapConverter;
import com.example.demo.helper.*;
import com.example.demo.services.ArticleService;
import com.example.demo.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("panel")
@SessionAttributes("articleSearchCriteria")
public class ArticlePanelController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Value("")
    private static final int ARTICLES_PER_PAGE = 10;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SortModeHelper sortModeHelper;

    @Autowired
    private PageSizeHelper pageSizeHelper;

    @Autowired
    private PagerParamsHelper pagerParamsHelper;

    @Autowired
    private FormHelperFactory formHelperFactory;


    @ModelAttribute("articleSearchCriteria")
    public ArticleSearchForm setUpArticleSearchCriteria() {
        return new ArticleSearchForm();
    }

    @GetMapping()
    public String home (Model model,
                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                        @ModelAttribute("articleSearchCriteria") ArticleSearchForm articleSearchCriteria
                        ){


        logger.info("Articles list");
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                                            ArticleSearchFormMapConverter.from(articleSearchCriteria)));
        model.addAttribute("articles", articleService.getAllArticles(articleSearchCriteria, pageable));
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("title", "Article list");

        return "panel/index";
    }

    @PostMapping()
    public String searchForm (Model model,
                              @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                              @Valid ArticleSearchForm articleSearchForm,
                              BindingResult bindingResult,
                              @ModelAttribute(value = "articleSearchCriteria", binding = false) ArticleSearchForm articleSearchCriteria
                             ){

        logger.info("Articles - receive ArticleSearchForm");

        if (!bindingResult.hasErrors()) {
            articleSearchCriteria = articleSearchForm;
            model.addAttribute("articleSearchCriteria", articleSearchCriteria);
        }
        model.addAttribute("articles", articleService.getAllArticles(articleSearchCriteria, pageable));
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("title", "Article list");

        return "panel/index";
    }

    @PostMapping("delete")
    public String deleteForm (Model model,
                              @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                              @RequestParam(required = false) Long[] ids,
                              RedirectAttributes redirectAttributes) {

        int count = 0;
        if (ids != null)
             count = articleService.deleteArticles(Arrays.asList(ids));

        redirectAttributes.addFlashAttribute("alertInfo", count+" articles have been deleted.");

        logger.info("Articles - delete {} articles", count);
        String params = new PagerParamsHelper().getParamsString(pageable);

        return "redirect:/panel?"+params;
    }

    @ModelAttribute
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("pagerParamsHelper", pagerParamsHelper);
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }
}
