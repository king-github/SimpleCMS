package com.example.demo.form.panel;


import com.example.demo.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TagFormTagConverter {


    public Tag toTag(TagForm tagForm) {

        return new Tag(tagForm.getId(), tagForm.getName());
    }

    public TagForm toTagForm(Tag tag) {

        return new TagForm(tag.getId(), tag.getName());
    }

    public Map<String, Object> toMap (TagForm tagForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", tagForm.getId());
        map.put("name", tagForm.getName());

        return map;
    }
}
