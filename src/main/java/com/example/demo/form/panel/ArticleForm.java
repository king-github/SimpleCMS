package com.example.demo.form.panel;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
public class ArticleForm {

    private Long id;
    @Size(min=3, max = 100)
    private String title;
    @Size(min=3, max = 100)
    private String lead;
    @Size(min=3, max = 200)
    private String content;

    private boolean published;

    private Long sectionId;

    private Set<Long> tagIds = new HashSet<>();


    public ArticleForm(Long id, String author, String title, String lead, String content, boolean published,
                       Long sectionId, Set<Long> tagIds,
                       LocalDateTime publicationDate, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        this.id = id;

        this.title = title;
        this.lead = lead;
        this.content = content;
        this.published = published;
        this.sectionId = sectionId;
        this.tagIds = tagIds;
    }

    public ArticleForm() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
