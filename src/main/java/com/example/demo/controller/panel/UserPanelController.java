package com.example.demo.controller.panel;


import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.PageSizeHelper;
import com.example.demo.helper.PagerParamsHelper;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("panel/user")
@SessionAttributes("pager")
public class UserPanelController {

    private static final int USERS_PER_PAGE = 10;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("userPanelOrderModeHelper")
    private OrderModeHelper sortModeHelper;

    @Autowired
    private PageSizeHelper pageSizeHelper;

    @ModelAttribute("pager")
    private PageRequest setUpPageRequest() { return  PageRequest.of(0, USERS_PER_PAGE, Sort.by("username")); }


    @GetMapping(value = "")
    public String index (Model model,
                         @PageableDefault(size = USERS_PER_PAGE) Pageable currentPager,
                         @ModelAttribute(value = "pager", binding = false) PageRequest pager
                        ) {


        pager = PageRequest.of(currentPager.getPageNumber(), currentPager.getPageSize(), currentPager.getSort());
        model.addAttribute("pager", pager);

        model.addAttribute("users", userService.getAllUsers(pager));

        model.addAttribute("sortModeHelper", sortModeHelper);
        model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(pager));
        model.addAttribute("pageSizeHelper", pageSizeHelper);
        model.addAttribute("title", "User list");

//        model.addAttribute("form", formHelperFactory
//                .makeErrorFormHelper(tagFormTagConverter.toMap(new TagForm())));

        return "panel/user/index";
    }


//    @PostMapping("delete")
//    public String delete(Long tagId,
//                         RedirectAttributes redirectAttributes,
//                         @ModelAttribute(value = "sort", binding = false) Sort sort
//                        ) {
//
//        Optional<TagWithQuantityDto> deleted = tagService.deleteTagById(tagId);
//
//        if (deleted.isPresent()) {
//            redirectAttributes.addFlashAttribute("alertInfo",
//                    String.format("Tag %s has been deleted and %d related articles have been updated.",
//                            deleted.get().getName(), deleted.get().getQuantity()));
//        } else {
//            redirectAttributes.addFlashAttribute("alertDanger",
//                    "Tag no exist.");
//        }
//
//        return "redirect:/panel/tag?" +PagerParamsHelper.of(sort).build();
//    }
//
//    @PostMapping("edit")
//    public String edit(Model model,
//                       @Valid TagForm tagForm,
//                       BindingResult bindingResult,
//                       RedirectAttributes redirectAttributes,
//                       @ModelAttribute(value = "sort", binding = false) Sort sort
//                       ) {
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("tags", tagService.getAllTagsWithQuantity(
//                    PageRequest.of(0, Integer.MAX_VALUE, sort)));
//            model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
//            model.addAttribute("alertDanger", "Tag has not been saved! Form has some errors.");
//
//            model.addAttribute("sortModeHelper", sortModeHelper);
//            model.addAttribute("pagerParamsHelper", PagerParamsHelper.of(sort));
//            return "panel/tag/index";
//        }
//
//        Tag result = tagService.save(tagFormTagConverter.toTag(tagForm));
//        redirectAttributes.addFlashAttribute("alertInfo",
//                    String.format("Tag %s has been saved.", tagForm.getName()));
//
//        return "redirect:/panel/tag?" +PagerParamsHelper.of(sort).build();
//    }

}
