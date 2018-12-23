package com.example.demo.form.panel;


import com.example.demo.entity.Section;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class SectionFormSectionConverter {


    public Section toSection(SectionForm sectionForm) {

        return new Section(sectionForm.getId(), sectionForm.getName());
    }

    public SectionForm toSectionForm(Section section) {

        return new SectionForm(section.getId(), section.getName());
    }

    public Map<String, Object> toMap (SectionForm sectionForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", sectionForm.getId());
        map.put("name", sectionForm.getName());

        return map;
    }
}
