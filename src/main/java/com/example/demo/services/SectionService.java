package com.example.demo.services;

import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    List<Section> getAllSections();

    List<SectionWithQuantityDto> getAllSectionsWithQuantity(Sort sort);

    Optional<SectionWithQuantityDto> delete(Long sectionId);

    Section save(Section section);

}
