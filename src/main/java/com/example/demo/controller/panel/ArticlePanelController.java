package com.example.demo.controller.panel;

import com.example.demo.controller.front.ArticleController;
import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import com.example.demo.form.panel.ArticleForm;
import com.example.demo.form.panel.ArticleFormArticleConverter;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.form.panel.ArticleSearchFormMapConverter;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PageSizeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.services.ArticleService;
import com.example.demo.services.SectionService;
import com.example.demo.services.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller

@RequestMapping("panel/article")
@SessionAttributes({"articleSearchCriteria","pager"})
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
    private TagService tagService;

    @ModelAttribute("articleSearchCriteria")
    public ArticleSearchForm setUpArticleSearchCriteria() {
        return new ArticleSearchForm();
    }

    @ModelAttribute("pager")
    public PageRequest setUpPageRequest() {
        return PageRequest.of(0, ARTICLES_PER_PAGE, Sort.unsorted());
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('SHOW_ARTICLES')")
    public String index (Model model,
                         @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                         @ModelAttribute("articleSearchCriteria") ArticleSearchForm articleSearchCriteria,
                         @ModelAttribute("pager") PageRequest pager
                        ){

        pager = (PageRequest) pageable;
        model.addAttribute("pager", pager);


        Long lastModifiedId = (Long) model.asMap().get("lastModifiedId");

        Page<Article> articles = articleService.getAllArticles(articleSearchCriteria, pageable);

        if (lastModifiedId != null
            && !articles.stream().anyMatch(article -> article.getId().equals(lastModifiedId))) {

            articleService.findArticleById(lastModifiedId)
                    .ifPresent(article -> model.addAttribute("lastModifiedArticle", article));
        }

        logger.info("Articles list");

        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                                            ArticleSearchFormMapConverter.from(articleSearchCriteria)));
        model.addAttribute("articles", articles);
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pageable));
        model.addAttribute("title", "Article list");

        addDefaultAttributeToModel(model);
        return "panel/article/index";
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('SHOW_ARTICLES')")
    public String searchForm (Model model,
                              @ModelAttribute("pager") PageRequest pager,
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
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pager));
        model.addAttribute("articles", articleService.getAllArticles(articleSearchCriteria, pager));
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
        model.addAttribute("title", "Article list");

        addDefaultAttributeToModel(model);
        return "panel/article/index";
    }


    @PostMapping("delete")
    @PreAuthorize("hasAuthority('DELETE_ARTICLES')")
    public String delete (Model model,
                          @RequestParam(required = false) Optional<List<Long>> ids,
                          @ModelAttribute("pager") PageRequest pager,
                          RedirectAttributes redirectAttributes) {

        int count = articleService.deleteArticles(ids.orElseGet(() -> new ArrayList<>()));

        redirectAttributes.addFlashAttribute("alertInfo", count+" articles have been deleted.");

        logger.info("Articles - delete {} articles", count);
        String params = PagerParamsHelper.of(pager).build();

        addDefaultAttributeToModel(model);
        return "redirect:/panel/article?"+params;
    }

    @GetMapping(value = {"edit/{id}", "edit"})        // for update or create
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String edit (Model model,
                        @PathVariable(required = false) Optional<Long> id) {

        logger.info("Articles - edit article with id: {}", id);

        Article article = id.isPresent() ? articleService.findArticle(id.get()) : new Article();
        ArticleForm articleForm = articleFormArticleConverter.toArticleForm(article);

        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                                                                articleFormArticleConverter.toMap(articleForm)));
        model.addAttribute("article", article);
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("title", "Edit article: ");

        addDefaultAttributeToModel(model);
        return "panel/article/edit";
    }

    @PostMapping(value = {"edit/{id}", "edit"})       // for update or create
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String save (Model model,
                        @PathVariable(required = false) Optional<Long> id,
                        @Valid ArticleForm articleForm,
                        BindingResult bindingResult,
                        @ModelAttribute("pager") PageRequest pager,
                        RedirectAttributes redirectAttributes) {

        logger.info("Articles - save article with id: {}", id);

        Article article = (id.isPresent()) ? articleService.findArticle(id.get()) : new Article();

        if(!bindingResult.hasErrors()) {
            article = articleService.save(articleFormArticleConverter.toArticle(articleForm, article));

            String info = ((id.isPresent()) ? "Article has been saved " : "New article has been added ")
                        + ((article.isPublished()) ? "and is published." : " but is not published.");

            if (articleForm.getSubmit().equals("back")) {
                redirectAttributes.addFlashAttribute("alertInfo", info);
                redirectAttributes.addFlashAttribute("lastModifiedId", article.getId());

                String params = PagerParamsHelper.of(pager).build();

                return "redirect:/panel/article?"+params;
            }
            model.addAttribute("alertInfo", info);
        } else {
            model.addAttribute("alertDanger", "Article has not been saved! Form has some errors.");
        }

        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
        model.addAttribute("article", article);
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("title", "Edit article: ");

        addDefaultAttributeToModel(model);
        return "panel/article/edit";
    }


    //@ModelAttribute               // conflict with PreAuthorize
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ModelAndView notFoundErrorHandler(HttpServletRequest request, Exception exception,
                                             RedirectAttributes redirectAttributes,
                                             Authentication authentication) {

        PageRequest pager = (PageRequest) request.getSession().getAttribute("pager");
        redirectAttributes.addFlashAttribute("alertDanger","Access denied. You cannot perform this operation.");

        if (authentication != null && authentication.getPrincipal() instanceof User) {

            User user = (User) authentication.getPrincipal();

            if (user.hasAuthority("SHOW_ARTICLES"))
                return new ModelAndView("redirect:/panel/article?" + PagerParamsHelper.of(pager).build());

        }
        return  new ModelAndView("redirect:/panel");
    }
}
