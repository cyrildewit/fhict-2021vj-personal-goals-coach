package com.cyrildewit.pgc.application.controllers.front;

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

import com.cyrildewit.pgc.support.util.DateTimeFormatters;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.application.validation.form.auth.UserLoginFormRequest;

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

        boolean authenticated = authenticationService.attemptLogin(userLoginFormRequest.getEmail(), userLoginFormRequest.getPassword());

        if (!authenticated) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginFormRequest", result);
            redirectAttributes.addFlashAttribute("userLoginFormRequest", userLoginFormRequest);
            redirectAttributes.addFlashAttribute("message", "These credentials do not match our records.");
            redirectAttributes.addFlashAttribute("messageType", "danger");

            return "redirect:/login";
        }

        return "redirect:/";
    }
}