package com.example.demo.repository;


import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(nativeQuery = true)
    List<TagWithQuantityDto> countArticlesGroupedByTagName();

}
