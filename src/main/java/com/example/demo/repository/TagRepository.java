package com.example.demo.repository;


import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    // NameNativeQuery = Tag.countArticlesGroupedByTagName with SqlResultSetMapping
    @Query(countQuery = "SELECT COUNT(*) FROM tag AS t", nativeQuery = true)
    List<TagWithQuantityDto> countArticlesGroupedByTagName();

    //@Query(value="SELECT COUNT(*) FROM article_tag AS at WHERE at.tag_id = :tagId", nativeQuery = true)
    @Query(value="SELECT COUNT(*) " +
                 "FROM article_tag AS at " +
                 "LEFT JOIN article AS a ON a.id = at.article_id " +
                 "WHERE at.tag_id = :tagId " +
                 "AND a.published = TRUE",
            nativeQuery = true)
    Long countArticlesWithTagIdAndPublishedTrue(@Param("tagId") Long tagId);

    @Query(value="SELECT COUNT(*) " +
            "FROM article_tag AS at " +
            "LEFT JOIN article AS a ON a.id = at.article_id " +
            "WHERE at.tag_id = :tagId ",
            nativeQuery = true)
    Long countArticlesWithTagId(@Param("tagId") Long tagId);

    @Query(value="SELECT t.id, t.name, COUNT(at.tag_id) as quantity " +
                 "FROM article_tag AS at " +
                 "RIGHT JOIN tag t ON t.ID = at.tag_id " +
                 "LEFT JOIN article a ON a.ID = at.article_id " +
                 //"WHERE a.published IS NULL " +  // count tags related with 0 articles
                 "GROUP BY t.name " ,
            countQuery = "SELECT COUNT(*) FROM tag AS t",
            nativeQuery = true)
    List<Object[]> countArticlesGroupedByTagNameResultAsRaw(@Param("pageable") Pageable pageable);

    @Query(value="SELECT t.id, t.name, COUNT(at.tag_id) as quantity " +
            "FROM article_tag AS at " +
            "RIGHT JOIN tag t ON t.ID = at.tag_id " +
            "LEFT JOIN article a ON a.ID = at.article_id " +
            "WHERE a.published = TRUE " +
            "OR a.published IS NULL " +  // count tags related with 0 articles
            "GROUP BY t.name " ,
            countQuery = "SELECT COUNT(*) FROM tag AS t",
            nativeQuery = true)
    List<Object[]> countArticlesGroupedByTagNameAndPublishedResultAsRaw(@Param("pageable") Pageable pageable);


//    @Query(value="SELECT new com.example.demo.dto.TagWithQuantityDto(t.id, t.name, COUNT(t.id)) " +
//                 "FROM  Tag t" +
//                 "RIGHT JOIN com.example.demo.entity.Article a ON a.tags.id=t.id " +
//                 "WHERE t.id= :tagId " +
//                 "GROUP BY t.id")
//    Optional<TagWithQuantityDto> findTagByIdWithQuantity(@Param("tagId") Long tagId);
}
