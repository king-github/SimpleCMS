package com.example.demo.controller.panel;


import com.example.demo.controller.front.ArticleController;
import com.example.demo.entity.User;
import com.example.demo.error.NotFoundException;
import com.example.demo.form.panel.UserForm;
import com.example.demo.form.panel.UserFormUserConverter;
import com.example.demo.helper.*;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("panel/user")
@SessionAttributes("pager")
public class UserPanelController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

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

    @Autowired
    private FormHelperFactory formHelperFactory;

    @Autowired
    private UserFormUserConverter userFormUserConverter;


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

        return "panel/user/index";
    }

    @GetMapping("edit/{id}")
    public String edit(Model model,
                       @PathVariable Long id
    ) {

        logger.info("Edit user with id: {}", id);

        User user = userService.findUserById(id).orElseThrow(() -> new NotFoundException("User not found."));
        UserForm userForm = userFormUserConverter.toUserForm(user);

        model.addAttribute("statuses", User.UserStatus.getAllStatuses());
        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(userFormUserConverter.toMap(userForm)));
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("title", "User edit form");

        return "panel/user/edit";
    }

    @PostMapping("edit/{id}")
    public String save(Model model,
                       @PathVariable Long id,
                       @Valid UserForm userForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @ModelAttribute(value = "pager", binding = false) PageRequest pager
    ) {

        logger.info("Save user with id: {}", id);

        FormHelper formHelper;
        if (bindingResult.hasErrors()) {

            formHelper = formHelperFactory.makeErrorFormHelper(bindingResult);
            model.addAttribute("alertDanger","User has not been saved! Form has some errors.");
        } else {

            User user = userService.findUserById(id).orElseThrow(() -> new NotFoundException("User not found."));
            formHelper = formHelperFactory.makeErrorFormHelper(userFormUserConverter.toMap(userForm));
            user = userFormUserConverter.toUser(userForm, user);
            userService.save(user);

            String userHasBeenSavedMsg = String.format("User %s has been saved.", user.getUsername());

            if (userForm.getSubmit().equals("back")) {

                redirectAttributes.addFlashAttribute("alertInfo", userHasBeenSavedMsg);
                return "redirect:/panel/user?"+PagerParamsHelper.of(pager).build();
            }

            model.addAttribute("alertInfo", userHasBeenSavedMsg);
        }

        model.addAttribute("form", formHelper);
        model.addAttribute("statuses", User.UserStatus.getAllStatuses());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("title", "User edit form");

        return "panel/user/edit";
    }

}
