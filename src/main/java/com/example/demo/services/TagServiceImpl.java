package com.example.demo.services;

import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.error.NotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<TagWithQuantityDto> getAllTagsWithQuantity() {

        return tagRepository.countArticlesGroupedByTagName();
    }

    @Override
    public List<TagWithQuantityDto> getAllTagsWithQuantity(Pageable pageable) {

        List<TagWithQuantityDto> tagWithQuantityDtos =
                tagRepository.countArticlesGroupedByTagNameResultAsRaw(pageable).stream()
                        .map(ob -> new TagWithQuantityDto(((BigInteger) ob[0]).longValue(),
                                                          (String)ob[1],
                                                          ((BigInteger)ob[2]).longValue()))
                        .collect(Collectors.toList());

        return tagWithQuantityDtos;
    }


    @Override
    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag no exist"));
    }

    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findByName(name).orElseThrow(() -> new NotFoundException("Tag no exist"));
    }

    @Override
    public void addTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Optional<TagWithQuantityDto> deleteTagById(Long id) {

        Optional<Tag> optionalTag = tagRepository.findById(id);

        if (optionalTag.isPresent()) {

            // TODO - add method in native SQL to remove all rows with tag.id from article_tag table
            List<Article> articles = articleRepository.findAllArticlesByTagId(id);

            Tag tag = optionalTag.get();
            TagWithQuantityDto tagWithQuantityDto =
                    new TagWithQuantityDto(tag.getId(), tag.getName(), (long) articles.size());

            articles.stream().forEach(article -> {
                article.getTags().remove(tag);
                articleRepository.save(article);
            });

            articleRepository.flush();
            tagRepository.delete(tag);

            return Optional.of(tagWithQuantityDto);
        }

        return Optional.empty();
    }

    @Override
    public Tag save(Tag tag) {

        return tagRepository.save(tag);
    }

}
