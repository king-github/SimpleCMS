package com.example.demo.services;

import com.example.demo.entity.Article;
import com.example.demo.entity.Tag;
import com.example.demo.error.NotFoundException;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
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
    public void addTag(Article article) {

    }
}
