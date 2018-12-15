package com.example.demo.form.panel;

import com.example.demo.entity.Article;
import com.example.demo.entity.Section;
import com.example.demo.entity.Tag;
import com.example.demo.repository.SectionRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ArticleFormArticleConverter {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TagRepository tagRepository;

    public ArticleForm toArticleForm(Article article) {

        ArticleForm articleForm = new ArticleForm();

        articleForm.setId(article.getId());
        articleForm.setTitle(article.getTitle());

        articleForm.setLead(article.getLead());
        articleForm.setContent(article.getContent());

        articleForm.setPublished(article.isPublished());
        articleForm.setSectionId(Optional.ofNullable(article.getSection())
                                         .map(Section::getId)
                                         .orElse(null)
        );

        articleForm.setTagIds(article.getTags().stream()
                                               .map(Tag::getId)
                                               .collect(Collectors.toSet())
        );

        return articleForm;
    }

    public Article toArticle(ArticleForm articleForm, Article article) {

        article.setTitle(articleForm.getTitle());
        article.setLead(articleForm.getLead());
        article.setContent(articleForm.getContent());

        Optional<Section> section = sectionRepository.findById(articleForm.getSectionId());

        article.setSection(section.orElseThrow(() -> new ConvertException("Cannot convert ArticleForm to Article section cannot be null.")));

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(articleForm.getTagIds()));
        article.setTags(tags);
        article.setPublished(articleForm.isPublished());

        return article;
    }

    public Article toNewArticle(ArticleForm articleForm) {

        return  toArticle(articleForm, new Article());

    }

    public Map<String, Object> toMap (ArticleForm articleForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", articleForm.getId());
        map.put("title", articleForm.getTitle());

        map.put("lead", articleForm.getLead());
        map.put("content", articleForm.getContent());

        map.put("sectionId", articleForm.getSectionId());
        map.put("tagIds", articleForm.getTagIds());
        map.put("published", articleForm.isPublished());

        return map;
    }
}
