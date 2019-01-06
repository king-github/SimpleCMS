package com.example.demo.services;

import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import com.example.demo.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Primary
@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public List<Section> getAllSections() {

        return sectionRepository.findAll();
    }

    @Override
    public List<SectionWithQuantityDto> getAllSectionsWithQuantity(Sort sort) {

        return sectionRepository.getAllSectionsWithQuantity(sort);
    }

    @Override
    @Transactional()
    public Optional<SectionWithQuantityDto> delete(Long sectionId) {

        Optional<SectionWithQuantityDto> sectionWithQuantity = sectionRepository.findSectionByIdWithQuantity(sectionId);

        if (sectionWithQuantity.isPresent() && sectionWithQuantity.get().getQuantity() == 0) {
            sectionRepository.deleteById(sectionId);
            return sectionWithQuantity;
        }
        return Optional.empty();
    }

    @Override
    public Section save(Section section) {

        return sectionRepository.save(section);
    }
}
