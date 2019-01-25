package com.example.demo.controller.panel;

import com.example.demo.dto.SectionWithQuantityDto;
import com.example.demo.entity.Section;
import com.example.demo.form.panel.SectionForm;
import com.example.demo.form.panel.SectionFormSectionConverter;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger logger = LoggerFactory.getLogger(SectionPanelController.class);

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
    @PreAuthorize("hasAuthority('SHOW_SECTIONS')")
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

        model.addAttribute("title", "Section list");

        logger.info("Section list");
        return "panel/section/index";
    }


    @PostMapping("delete")
    @PreAuthorize("hasAuthority('DELETE_SECTIONS')")
    public String delete(Long sectionId,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute(value = "sort", binding = false) Sort sort
                        ) {

        Optional<SectionWithQuantityDto> deleted = sectionService.delete(sectionId);

        if (deleted.isPresent()) {
            redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been deleted.", deleted.get().getName()));

            logger.info("Section list -section with id: {} has been deleted", deleted.get().getId() );
        } else {
            redirectAttributes.addFlashAttribute("alertDanger",
                    "Section no exist or has related with some articles.");
        }

        return "redirect:/panel/section?" +PagerParamsHelper.of(sort).build();
    }

    @PostMapping("edit")
    @PreAuthorize("hasAuthority('EDIT_SECTIONS')")
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
            model.addAttribute("title", "Section list");

            logger.info("Section list - save section with id: {} failed", sectionForm.getId() );
            return "panel/section/index";
        }

        Section result = sectionService.save(sectionFormSectionConverter.toSection(sectionForm));
        redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Section %s has been saved.", result.getName()));

        logger.info("Section list - save section with id: {}", result.getId() );
        return "redirect:/panel/section?" +PagerParamsHelper.of(sort).build();
    }

}
