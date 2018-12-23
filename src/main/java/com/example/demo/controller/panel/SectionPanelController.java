package com.example.demo.controller.panel;

import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import com.example.demo.form.panel.SectionForm;
import com.example.demo.form.panel.SectionFormSectionConverter;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("panel/section")
public class SectionPanelController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionFormSectionConverter sectionFormSectionConverter;

    @Autowired
    private FormHelperFactory formHelperFactory;

    @GetMapping()
    public String index (Model model) {

        model.addAttribute("sections", sectionService.getAllSectionsWithQuantity());
        model.addAttribute("form", formHelperFactory
                .makeErrorFormHelper(sectionFormSectionConverter.toMap(new SectionForm())));
        return "panel/section/index";
    }


    @PostMapping("delete")
    public String delete(Long sectionId,
                         RedirectAttributes redirectAttributes) {

        Optional<SectionWithQuantityDto> deletd = sectionService.delete(sectionId);

        if (deletd.isPresent()) {
            redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been deleted.", deletd.get().getName()));
        } else {
            redirectAttributes.addFlashAttribute("alertDanger",
                    "Section no exist or has related with some articles.");
        }

        return "redirect:/panel/section";
    }

    @PostMapping("edit")
    public String edit(Model model,
                       @Valid SectionForm sectionForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("sections", sectionService.getAllSectionsWithQuantity());
            model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
            model.addAttribute("alertDanger", "Section has not been saved! Form has some errors.");
            return "panel/section/index";
        }

        Section result = sectionService.save(sectionFormSectionConverter.toSection(sectionForm));
        redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been saved.", sectionForm.getName()));

        return "redirect:/panel/section";
    }


}
