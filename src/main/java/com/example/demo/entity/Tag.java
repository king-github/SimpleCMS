package com.example.demo.entity;

import com.example.demo.dto.TagWithQuantityDto;

import javax.persistence.*;


@SqlResultSetMapping(
        name="tagDtoMapping",
        classes={
            @ConstructorResult(
                targetClass= TagWithQuantityDto.class,
                columns={@ColumnResult(name="id", type = Long.class),
                         @ColumnResult(name="name", type = String.class),
                         @ColumnResult(name="quantity", type = Integer.class)
                        }
            )
        }
)

@NamedNativeQuery(name="Tag.countArticlesGroupedByTagName",
        query = "SELECT t.id, t.name, count(at.tag_id) as quantity " +
                "FROM ARTICLE_TAG AS at " +
                "LEFT JOIN TAG t ON t.ID = at.tag_id " +    // change to RIGHT JOIN to list all tags with quantity >= 0
                "GROUP BY t.name " +
                "ORDER BY quantity DESC",
        resultSetMapping="tagDtoMapping")

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

}
