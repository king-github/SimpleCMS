package com.example.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
  @NamedEntityGraph(name = "allFetch", attributeNodes = {
        @NamedAttributeNode("author"),
        @NamedAttributeNode("section"),
        @NamedAttributeNode("tags")
  }),
  @NamedEntityGraph(name = "noFetchTags", attributeNodes = {
        @NamedAttributeNode("author"),
        @NamedAttributeNode("section")
  })
})

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime publicationDate;
    private String title;
    private String lead;
    private String content;

    private boolean published = false;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private Section section;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "article_tag",
               joinColumns = @JoinColumn(name = "article_id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OrderBy("name ASC")
    private Set<Tag> tags = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public Article() {
    }

    public Article(String title, String lead, String content, Author author) {
        this.publicationDate = LocalDateTime.now();
        this.title = title;
        this.lead = lead;
        this.content = content;
        this.author = author;
    }

    @PrePersist
    @PreUpdate
    private void upadatePublicationDate() {
        if (getPublicationDate() == null && isPublished()) {
            setPublicationDate(LocalDateTime.now());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
