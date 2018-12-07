package com.example.demo.controller.panel;

import com.example.demo.controller.front.ArticleController;
import com.example.demo.entity.Article;
import com.example.demo.entity.Author;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.helper.*;
import com.example.demo.services.ArticleService;
import com.example.demo.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("panel")
public class ArticlePanelController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
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
    private ErrorFormHelperFactory errorFormHelperFactory;

    @GetMapping(value = "")
    public String home (Model model,
                        @PageableDefault(size = ARTICLES_PER_PAGE) Pageable pageable,
                        @Valid ArticleSearchForm articleSearchForm,
                        BindingResult bindingResult
                        ){


        //articleSearchForm.setDateFrom(LocalDate.now());
        System.out.println(articleSearchForm);

       // System.out.println( "DTA: "+LocalDateTime.parse("2018-02-02 12:22", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString() );

        Article article = new Article();
        article.setTitle("1");
        Author author = new Author();
        author.setLastname("B");
        article.setAuthor(author);

        model.addAttribute("errors", errorFormHelperFactory.makeErrorFormHelper(bindingResult));

        model.addAttribute("info", articleSearchForm.toString());
        model.addAttribute("articleSearchForm", articleSearchForm);
        //model.addAttribute("articles", articleService.findArticlesByExample(article, pageable));
        model.addAttribute("articles", articleService.getAllArticles(articleSearchForm, pageable));

        logger.info("Articles");

        //model.addAttribute("articles", articleService.getAllArticles( pageable ));
        model.addAttribute("sections", sectionService.getAllSections());
        model.addAttribute("title", "Article list");

        return "panel/index";
    }


//    @InitBinder("articleSearchForm")
//    public void initBinder(WebDataBinder binder) {
//
//        System.out.println("HELLO INIT BINDER: ");
//
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormatter.setLenient(false);
//        binder.registerCustomEditor(Date.class,
//                new CustomDateEditor(dateFormatter, true));
//
//
//
//        binder.registerCustomEditor(
//                LocalDateTime.class,
//                new PropertyEditorSupport() {
//                        @Override
//                        public void setAsText(String text) throws IllegalArgumentException {
//                            System.out.println("BINDER: "+text);
//                            LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//                        }
//                });
//    }

    @ModelAttribute
    private void addDefaultAttributeToModel(Model model) {
        model.addAttribute("dtFormatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("pagerParamsHelper", pagerParamsHelper);
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pageSizeHelper", pageSizeHelper);
    }
}
