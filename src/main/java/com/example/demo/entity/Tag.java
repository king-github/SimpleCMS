package com.example.demo.entity;

import com.example.demo.dto.TagWithQuantityDto;

import javax.persistence.*;
import java.util.Objects;


@SqlResultSetMapping(
        name="tagDtoMapping",
        classes={
            @ConstructorResult(
                targetClass= TagWithQuantityDto.class,
                columns={@ColumnResult(name="id", type = Long.class),
                         @ColumnResult(name="name", type = String.class),
                         @ColumnResult(name="quantity", type = Long.class)
                        }
            )
        }
)

@NamedNativeQuery(name = "Tag.countArticlesGroupedByTagName",
        query = "SELECT t.id, t.name, COUNT(at.tag_id) as quantity " +
                "FROM article_tag AS at " +
                "LEFT JOIN tag t ON t.ID = at.tag_id " +
                "LEFT JOIN article a ON a.ID = at.article_id " +
                "WHERE a.published = TRUE " +
                "GROUP BY t.id " +
                "ORDER BY quantity DESC",
        resultSetMapping = "tagDtoMapping")

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id != null && id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
