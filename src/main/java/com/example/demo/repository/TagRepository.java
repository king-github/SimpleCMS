package com.example.demo.repository;


import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(nativeQuery = true)
    List<TagWithQuantityDto> countArticlesGroupedByTagName();

    @Query(value="SELECT COUNT(*) FROM ARTICLE_TAG AS AT WHERE AT.TAG_ID = :tagId", nativeQuery = true)
    Integer countArticlesWithTagId(@Param("tagId") Long tagId);

}
