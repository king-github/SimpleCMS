package com.example.demo.helper;

import com.example.demo.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagHelperFactory {

    @Autowired
    private TagService tagService;

    public TagHelper make(){

        return new TagHelper(tagService.getAllTags());
    }

}
