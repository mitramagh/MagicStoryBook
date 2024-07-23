package com.example.magicstorybook.controller;



import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
        }
        return "home";  // This should match the name of your Thymeleaf template file (home.html)
    }
//    @GetMapping
//    public Map<String, Object> currentUser(OAuth2AuthenticationToken token) {
//        return token.getPrincipal().getAttributes();
//    }


}
