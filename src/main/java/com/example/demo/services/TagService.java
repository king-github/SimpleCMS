package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    Tag findTagById(Long id);

    Tag findTagByName(String name);

    void addTag(Article article);

}
