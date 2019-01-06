package com.example.demo.controller.panel;

import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import com.example.demo.form.panel.SectionForm;
import com.example.demo.form.panel.SectionFormSectionConverter;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("panel/section")
@SessionAttributes("sort")
public class SectionPanelController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionFormSectionConverter sectionFormSectionConverter;

    @Autowired
    private FormHelperFactory formHelperFactory;

    @Autowired
    @Qualifier("sectionPanelOrderModeHelper")
    private OrderModeHelper sortModeHelper;

    @ModelAttribute("sort")
    private Sort setUpSort() { return  Sort.by("name"); }


    @GetMapping(value = "")
    public String index (Model model,
                         Sort sortCurrent,
                         @ModelAttribute(value = "sort", binding = false) Sort sort
                        ) {

        sort = sortCurrent;
        model.addAttribute("sort", sort);
        model.addAttribute("sections", sectionService.getAllSectionsWithQuantity(sort));
        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(sort));
        model.addAttribute("sort", sort);

        model.addAttribute("form", formHelperFactory
                .makeErrorFormHelper(sectionFormSectionConverter.toMap(new SectionForm())));

        return "panel/section/index";
    }


    @PostMapping("delete")
    public String delete(Long sectionId,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute(value = "sort", binding = false) Sort sort
                        ) {

        Optional<SectionWithQuantityDto> deleted = sectionService.delete(sectionId);

        if (deleted.isPresent()) {
            redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been deleted.", deleted.get().getName()));
        } else {
            redirectAttributes.addFlashAttribute("alertDanger",
                    "Section no exist or has related with some articles.");
        }

        return "redirect:/panel/section?" +PagerParamsHelper.of(sort).build();
    }

    @PostMapping("edit")
    public String edit(Model model,
                       @Valid SectionForm sectionForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @ModelAttribute(value = "sort", binding = false) Sort sort
                       ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("sections", sectionService.getAllSectionsWithQuantity(sort));
            model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
            model.addAttribute("alertDanger", "Section has not been saved! Form has some errors.");

            model.addAttribute("sortModeHelper", sortModeHelper);
            model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(sort));
            return "panel/section/index";
        }

        Section result = sectionService.save(sectionFormSectionConverter.toSection(sectionForm));
        redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been saved.", sectionForm.getName()));

        return "redirect:/panel/section?" +PagerParamsHelper.of(sort).build();
    }

}
