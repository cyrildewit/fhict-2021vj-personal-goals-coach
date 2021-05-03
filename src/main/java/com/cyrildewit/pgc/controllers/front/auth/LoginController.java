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
import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.Subgoal;
import com.cyrildewit.pgc.models.User;
import com.cyrildewit.pgc.services.AuthenticationService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.validation.form.UserLoginFormRequest;

@Controller
public class LoginController {
    private final AuthenticationService authenticationService;

    @Autowired
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("userLoginFormRequest")) {
            model.addAttribute("userLoginFormRequest", new UserLoginFormRequest());
        }

        return "front/auth/login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("userLoginFormRequest") @Valid UserLoginFormRequest userLoginFormRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginFormRequest", result);
            redirectAttributes.addFlashAttribute("userLoginFormRequest", userLoginFormRequest);

            return "redirect:/login";
        }


        return "redirect:/";
    }
}