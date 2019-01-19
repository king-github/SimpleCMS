package com.example.demo.controller.panel;

import com.example.demo.controller.front.ArticleController;
import com.example.demo.entity.Article;
import com.example.demo.form.panel.ArticleForm;
import com.example.demo.form.panel.ArticleFormArticleConverter;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.form.panel.ArticleSearchFormMapConverter;
import com.example.demo.helper.*;
import com.example.demo.services.ArticleService;
import com.example.demo.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("panel/article")
@SessionAttributes("articleSearchCriteria")
public class ArticlePanelController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private static final int ARTICLES_PER_PAGE = 10;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    @Qualifier("articlePanelOrderModeHelper")
    private OrderModeHelper sortModeHelper;

    @Autowired
    private PageSizeHelper pageSizeHelper;

    @Autowired
    private FormHelperFactory formHelperFactory;

    @Autowired
    private ArticleFormArticleConverter articleFormArticleConverter;

    @Autowired
    private  TagHelperFactory tagHelperFactory;

    @ModelAttribute("articleSearchCriteria")
    public ArticleSearchForm setUpArticleSearchCriteria() {
        return new ArticleSearchForm();
    }

    @GetMapping()
    public String index (Model model,
                         @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                         @ModelAttribute("articleSearchCriteria") ArticleSearchForm articleSearchCriteria
                        ){

        logger.info("Articles list");
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                                            ArticleSearchFormMapConverter.from(articleSearchCriteria)));
        model.addAttribute("articles", articleService.getAllArticles(articleSearchCriteria, pageable));
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));

        model.addAttribute("title", "Article list");

        return "panel/article/index";
    }

    @PostMapping()
    public String searchForm (Model model,
                              @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                              @Valid ArticleSearchForm articleSearchForm,
                              BindingResult bindingResult,
                              @ModelAttribute(value = "articleSearchCriteria", binding = false)
                                                                            ArticleSearchForm articleSearchCriteria
                             ){

        logger.info("Articles - receive ArticleSearchForm");

        if (!bindingResult.hasErrors()) {
            articleSearchCriteria = articleSearchForm;
            model.addAttribute("articleSearchCriteria", articleSearchCriteria);
        }
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));
        model.addAttribute("articles", articleService.getAllArticles(articleSearchCriteria, pageable));
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
        model.addAttribute("title", "Article list");

        return "panel/article/index";
    }

    @PostMapping("delete")
    public String delete (Model model,
                          @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                          @RequestParam(required = false) Optional<List<Long>> ids,
                          RedirectAttributes redirectAttributes) {

        int count = articleService.deleteArticles(ids.orElseGet(() -> new ArrayList<>()));

        redirectAttributes.addFlashAttribute("alertInfo", count+" articles have been deleted.");

        logger.info("Articles - delete {} articles", count);
        String params = PagerParamsHelper.of(pageable).build();

        return "redirect:/panel/article?"+params;
    }

    @GetMapping(value = {"edit/{id}", "edit"})
    public String edit (Model model,
                        @PathVariable(required = false) Optional<Long> id) {

        logger.info("Articles - edit article with id: {}", id);

        Article article = id.isPresent() ? articleService.findArticle(id.get()) : new Article();
        ArticleForm articleForm = articleFormArticleConverter.toArticleForm(article);

        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                                                                articleFormArticleConverter.toMap(articleForm)));
        model.addAttribute("article", article);
        model.addAttribute("tagHelper", tagHelperFactory.make());
        model.addAttribute("title", "Edit article: ");
        return "panel/article/edit";
    }

    @PostMapping(value = {"edit/{id}", "edit"})
    public String save (Model model,
                        @PathVariable(required = false) Optional<Long> id,
                        @Valid ArticleForm articleForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        logger.info("Articles - save article with id: {}", id);

        Article article = (id.isPresent()) ? articleService.findArticle(id.get()) : new Article();

        if(!bindingResult.hasErrors()) {
            article = articleService.save(articleFormArticleConverter.toArticle(articleForm, article));

            String info = ((id.isPresent()) ? "Article has been saved " : "New article has been added ")
                        + ((article.isPublished()) ? "and is published." : " but is not published.");

            if (articleForm.getSubmit().equals("back")) {
                redirectAttributes.addFlashAttribute("alertInfo", info);
                return "redirect:/panel/article/";
            }
            model.addAttribute("alertInfo", info);
        } else {
            model.addAttribute("alertDanger", "Article has not been saved! Form has some errors.");
        }

        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
        model.addAttribute("article", article);
        model.addAttribute("tagHelper", tagHelperFactory.make());
        model.addAttribute("title", "Edit article: ");
        return "panel/article/edit";
    }


    @ModelAttribute
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }
}
