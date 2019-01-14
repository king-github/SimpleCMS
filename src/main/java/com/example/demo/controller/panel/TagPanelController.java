package com.example.demo.controller.panel;


import com.example.demo.dto.TagWithQuantityDto;
import com.example.demo.entity.Tag;
import com.example.demo.form.panel.TagForm;
import com.example.demo.form.panel.TagFormTagConverter;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("panel/tag")
@SessionAttributes("sort")
public class TagPanelController {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagFormTagConverter tagFormTagConverter;

    @Autowired
    private FormHelperFactory formHelperFactory;

    @Autowired
    @Qualifier("tagPanelOrderModeHelper")
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
        model.addAttribute("tags", tagService.getAllTagsWithQuantity(
                PageRequest.of(0, Integer.MAX_VALUE, sort)));

        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(sort));
        model.addAttribute("sort", sort);

        model.addAttribute("form", formHelperFactory
                .makeErrorFormHelper(tagFormTagConverter.toMap(new TagForm())));

        return "panel/tag/index";
    }


    @PostMapping("delete")
    public String delete(Long tagId,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute(value = "sort", binding = false) Sort sort
                        ) {

        Optional<TagWithQuantityDto> deleted = tagService.deleteTagById(tagId);

        if (deleted.isPresent()) {
            redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Tag %s has been deleted and %d related articles have been updated.",
                            deleted.get().getName(), deleted.get().getQuantity()));
        } else {
            redirectAttributes.addFlashAttribute("alertDanger",
                    "Tag no exist.");
        }

        return "redirect:/panel/tag?" +PagerParamsHelper.of(sort).build();
    }

    @PostMapping("edit")
    public String edit(Model model,
                       @Valid TagForm tagForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @ModelAttribute(value = "sort", binding = false) Sort sort
                       ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("tags", tagService.getAllTagsWithQuantity(
                    PageRequest.of(0, Integer.MAX_VALUE, sort)));
            model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
            model.addAttribute("alertDanger", "Tag has not been saved! Form has some errors.");

            model.addAttribute("sortModeHelper", sortModeHelper);
            model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(sort));
            return "panel/tag/index";
        }

        Tag result = tagService.save(tagFormTagConverter.toTag(tagForm));
        redirectAttributes.addFlashAttribute("alertInfo",
                    String.format("Tag %s has been saved.", tagForm.getName()));

        return "redirect:/panel/tag?" +PagerParamsHelper.of(sort).build();
    }

}