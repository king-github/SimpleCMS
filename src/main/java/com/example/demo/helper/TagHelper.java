package com.example.demo.helper;

import com.example.demo.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class TagHelper {

    private List<Tag> allTags;

    public TagHelper(List<Tag> allTags) {
        this.allTags = allTags;
    }

    public static class TagItem {

        public TagItem(Long id, String name, boolean checked) {
            this.id = id;
            this.name = name;
            this.checked = checked;
        }

        public Long id;
        public String name;
        public boolean checked;

    }

    public List<TagItem> getTagItems(Collection<Long> ids) {

        return allTags.stream()
                      .map(tag -> new TagItem(tag.getId(), tag.getName(), ids.contains(tag.getId())))
                      .collect(Collectors.toList());

    }

}
