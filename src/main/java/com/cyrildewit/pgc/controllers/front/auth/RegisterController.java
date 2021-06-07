package com.cyrildewit.pgc.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.logic.model.Goal;
import com.cyrildewit.pgc.logic.model.Subgoal;
import com.cyrildewit.pgc.logic.model.User;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.services.AuthenticationService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.validation.form.auth.UserRegisterFormRequest;

@Controller
public class RegisterController {
    private final AuthenticationService authenticationService;

    @Autowired
    public RegisterController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("userRegisterFormRequest")) {
            model.addAttribute("userRegisterFormRequest", new UserRegisterFormRequest());
        }

        return "front/auth/register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("userRegisterFormRequest") @Valid UserRegisterFormRequest userRegisterFormRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterFormRequest", result);
            redirectAttributes.addFlashAttribute("userRegisterFormRequest", userRegisterFormRequest);

            return "redirect:/register";
        }

        return "redirect:/";
    }
}