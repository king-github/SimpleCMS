package com.example.demo.services;

import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TagService {

    List<Tag> getAllTags();

    List<TagWithQuantityDto> getAllTagsWithQuantity();

    List<TagWithQuantityDto> getAllTagsWithQuantity(Pageable pageable);

    Tag findTagById(Long id);

    Tag findTagByName(String name);

    void addTag(Tag tag);


    Optional<TagWithQuantityDto> deleteTagById(Long id);

    Tag save(Tag tag);
}
