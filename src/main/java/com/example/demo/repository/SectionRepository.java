package com.example.demo.repository;


import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT new com.example.demo.dto.SectionWithQuantityDto(s.id, s.name, COUNT(a.section.id)) " +
           "FROM Section s " +
           "LEFT JOIN com.example.demo.entity.Article a ON s=a.section " +
           "GROUP BY s")
    List<SectionWithQuantityDto> getAllSectionsWithQuantity();

    @Query("SELECT new com.example.demo.dto.SectionWithQuantityDto(s.id, s.name, COUNT(a.section.id)) " +
            "FROM Section s " +
            "LEFT JOIN com.example.demo.entity.Article a ON s=a.section " +
            "WHERE s.id=:sectionId " +
            "GROUP BY s")
    Optional<SectionWithQuantityDto> findSectionByIdWithQuantity(@Param("sectionId") Long sectionId);

}
