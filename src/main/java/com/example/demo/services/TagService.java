package com.example.demo.services;

import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    List<TagWithQuantityDto> getAllTagsWithQuantity();

    Tag findTagById(Long id);

    Tag findTagByName(String name);

    void addTag(Tag tag);
}
