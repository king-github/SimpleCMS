package com.example.demo.controller.panel;

import com.example.demo.entity.Article;
import com.example.demo.form.panel.ArticleForm;
import com.example.demo.form.panel.ArticleFormArticleConverter;
import com.example.demo.form.panel.ArticleSearchForm;
import com.example.demo.helper.FormHelper;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.TagHelperFactory;
import com.example.demo.services.ArticleService;
import com.example.demo.services.SectionService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class ArticlePanelControllerTest {

    private ArticlePanelController articlePanelController;
    @Mock
    private ArticleService articleServiceMock;
    @Mock
    private ArticleFormArticleConverter articleFormArticleConverterMock;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private RedirectAttributes redirectAttributes;
    @Mock
    private FormHelperFactory formHelperFactory;
    @Mock
    private FormHelper formHelperMock;
    @Mock
    private SectionService sectionService;
    @Mock
    private TagHelperFactory tagHelperFactory;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<Article> pageMock;

    @Before
    public void init() {

        articlePanelController = new ArticlePanelController();

        ReflectionTestUtils.setField(articlePanelController, "articleService", articleServiceMock);
        ReflectionTestUtils.setField(articlePanelController, "articleFormArticleConverter",
                                                                            articleFormArticleConverterMock);
        ReflectionTestUtils.setField(articlePanelController, "formHelperFactory", formHelperFactory);

        ReflectionTestUtils.setField(articlePanelController, "sectionService", sectionService);
        ReflectionTestUtils.setField(articlePanelController, "tagHelperFactory", tagHelperFactory);
    }

    @Test
    public void givenParams_whenIndex_thenReceiveListOfArticles () {

        ArticleSearchForm articleSearchCriteria = new ArticleSearchForm();

        when(articleServiceMock.getAllArticles(articleSearchCriteria, pageable)).thenReturn(pageMock);
        when(formHelperFactory.makeErrorFormHelper(anyMap())).thenReturn(formHelperMock);

        String result = articlePanelController.index(model, pageable, articleSearchCriteria);

        verify(articleServiceMock, times(1)).getAllArticles(articleSearchCriteria, pageable);
        verify(model, times(1)).addAttribute(eq("articles"), eq(pageMock));
        verify(model, times(1)).addAttribute(eq("form"), eq(formHelperMock));
        verify(model, times(1)).addAttribute(eq("title"), anyString());

        assertEquals("panel/article/index", result);
    }

    @Test
    public void givenCorrectSearchForm_whenIndex_thenSaveCriteria () {

        ArticleSearchForm articleSearchForm = new ArticleSearchForm();
        articleSearchForm.setTitle("articleSearchForm");
        ArticleSearchForm articleSearchCriteria = new ArticleSearchForm();
        articleSearchCriteria.setTitle("articleSearchCriteria");

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = articlePanelController
                            .searchForm(model, pageable, articleSearchForm, bindingResult, articleSearchCriteria);

        verify(articleServiceMock, times(1)).getAllArticles(articleSearchForm, pageable);
        verify(model).addAttribute(eq("articleSearchCriteria"), eq(articleSearchForm));

        assertEquals("panel/article/index", result);
    }

    @Test
    public void givenWrongSearchForm_whenSearchForm_thenGetPreviousCriteria () {

        ArticleSearchForm articleSearchForm = new ArticleSearchForm();
        articleSearchForm.setTitle("articleSearchForm");
        ArticleSearchForm articleSearchCriteria = new ArticleSearchForm();
        articleSearchCriteria.setTitle("articleSearchCriteria");

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = articlePanelController
                            .searchForm(model, pageable, articleSearchForm, bindingResult, articleSearchCriteria);

        verify(articleServiceMock, times(1)).getAllArticles(articleSearchCriteria, pageable);

        assertEquals("panel/article/index", result);
    }

    @Test
    public void givenIdsArticle_whenDelete_thenCallDeleteIds () {

        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        String result = articlePanelController.delete(model, pageable, Optional.of(ids), redirectAttributes);

        verify(articleServiceMock, times(1)).deleteArticles(ids);

        assertThat(result, Matchers.startsWith("redirect:/panel/article"));
    }

    @Test
    public void givenIdArticle_whenEdit_thenShowArticleFormWithId () {

        final Long ARTICLE_ID = 234L;
        ArticleForm articleForm = new ArticleForm();
        Article article1 = new Article();
        article1.setId(ARTICLE_ID);

        when(articleServiceMock.findArticle(anyLong()))
                .thenAnswer(invocation -> invocation.getArgument(0) == ARTICLE_ID ? article1 : null);
        when(articleFormArticleConverterMock.toArticleForm(any(Article.class))).thenReturn(articleForm);

        String result = articlePanelController.edit(model, Optional.of(ARTICLE_ID));

        verify(articleServiceMock, times(1)).findArticle(anyLong());
        verify(articleFormArticleConverterMock, times(1)).toArticleForm(article1);

        assertEquals("panel/article/edit", result);
    }

    @Test
    public void givenNoIdArticle_whenEdit_thenShowNewArticleForm () {

        ArticleForm articleForm = new ArticleForm();


        when(articleFormArticleConverterMock.toArticleForm(any(Article.class))).thenReturn(articleForm);

        String result = articlePanelController.edit(model, Optional.empty());

        verify(articleServiceMock, never()).findArticle(anyLong());
        verify(articleFormArticleConverterMock, times(1)).toArticleForm(any());

        assertEquals("panel/article/edit", result);
    }

    @Test
    public void givenCorrectArticleWithIdForm_whenSave_thenSaveArticleAndBAckToArticles () {

        final Long ARTICLE_ID = 176L;
        ArticleForm articleForm = new ArticleForm();
        articleForm.setSubmit("back");
        Article article = new Article();
        article.setId(ARTICLE_ID);

        when(articleServiceMock.findArticle(any())).thenReturn(article);
        when(articleServiceMock.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bindingResult.hasErrors()).thenReturn(false);

        when(articleFormArticleConverterMock.toArticle(articleForm, article))
                                            .thenAnswer(invocation -> invocation.getArgument(1));

        String result = articlePanelController
                            .save(model, Optional.of(12L), articleForm, bindingResult, redirectAttributes);

        assertEquals("redirect:/panel/article/", result);

        verify(articleServiceMock, times(1)).save(article);
        verify(articleFormArticleConverterMock, times(1)).toArticle(articleForm, article);
        verify(bindingResult, times(1)).hasErrors();

        verify(articleServiceMock).save(argThat(argument -> argument == article));
    }

    @Test
    public void givenCorrectArticleWithIdForm_whenSave_thenSaveArticleAndStayInForm () {

        final Long ARTICLE_ID = 176L;
        ArticleForm articleForm = new ArticleForm();
        articleForm.setSubmit("stay");
        Article article = new Article();
        article.setId(ARTICLE_ID);

        when(articleServiceMock.findArticle(any())).thenReturn(article);
        when(articleServiceMock.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bindingResult.hasErrors()).thenReturn(false);

        when(articleFormArticleConverterMock.toArticle(articleForm, article))
                .thenAnswer(invocation -> invocation.getArgument(1));

        String result = articlePanelController
                .save(model, Optional.of(12L), articleForm, bindingResult, redirectAttributes);

        assertEquals("panel/article/edit", result);

        verify(articleServiceMock, times(1)).save(article);
        verify(articleFormArticleConverterMock, times(1)).toArticle(articleForm, article);
        verify(bindingResult, times(1)).hasErrors();

        verify(articleServiceMock).save(argThat(argument -> argument == article));
    }


    @Test
    public void givenArticleFormWithoutId_whenSave_thenSaveNewArticle () {


        ArticleForm articleForm = new ArticleForm();
        articleForm.setSubmit("back");
        Article article1 = new Article();


        when(articleServiceMock.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bindingResult.hasErrors()).thenReturn(false);
        when(articleFormArticleConverterMock.toArticle(eq(articleForm), any(Article.class))).thenReturn(article1);

        String result = articlePanelController
                            .save(model, Optional.empty(), articleForm, bindingResult, redirectAttributes);

        assertEquals("redirect:/panel/article/", result);

        verify(articleServiceMock, times(1)).save(any(Article.class));
        verify(articleFormArticleConverterMock, times(1)).toArticle(any(), any());
        verify(bindingResult, times(1)).hasErrors();

        verify(articleServiceMock).save(argThat(argument -> argument == article1));
    }

    @Test
    public void givenArticleFormWithErrors_whenSave_thenNotSaveAndNotRedirect () {

        final Long ARTICLE_ID = 385L;

        ArticleForm articleForm = new ArticleForm();
        Article article1 = new Article();
        article1.setId(ARTICLE_ID);

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = articlePanelController
                                .save(model, Optional.empty(), articleForm, bindingResult, redirectAttributes);

        assertEquals("panel/article/edit", result);

        verify(articleServiceMock, never()).save(any(Article.class));
        verify(bindingResult, times(1)).hasErrors();

    }

}