package com.example.demo.controller.panel;

import com.example.demo.entity.Author;
import com.example.demo.form.panel.RegisterAuthorFormAuthorConverter;
import com.example.demo.form.panel.RegisterAuthorForm;
import com.example.demo.helper.FormHelperFactory;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("register")
public class RegisterAuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private FormHelperFactory formHelperFactory;

    @Autowired
    private RegisterAuthorFormAuthorConverter registerAuthorFormAuthorConverter;

    @GetMapping()
    public String register(Model model) {


        model.addAttribute("form", formHelperFactory.makeErrorFormHelper(
                registerAuthorFormAuthorConverter.toMap(new RegisterAuthorForm())));
        model.addAttribute("title", "Create new account");

        return "auth/register";
    }

    @PostMapping()
    public String register(Model model,
                           @Valid RegisterAuthorForm form,
                           BindingResult bindingResult) {


        if(!bindingResult.hasErrors()) {

            // TODO - username have to be unique!!!
            Author saved = authorRepository.save(registerAuthorFormAuthorConverter.toAuthor(form));
            return "redirect:/register/success";
        }

            model.addAttribute("form", formHelperFactory.makeErrorFormHelper(bindingResult));
            model.addAttribute("title", "Create new account");
            model.addAttribute("alertDanger","Account has not been created! Form has some errors.");

            return "auth/register";

    }

    @GetMapping("success")
    public String registerSuccess(Model model) {

        model.addAttribute("title", "New account has been created.");
        return "auth/register-success";
    }

}
