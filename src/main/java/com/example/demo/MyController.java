package com.example.demo;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Burkhard Graves
 */
@Controller
@RequiredArgsConstructor
public class MyController {
    
    private final SessionRegistryWrapper srw;

    @GetMapping
    public String index(Principal principal, Model model) {
        
        model.addAttribute("name", principal.getName());
        model.addAttribute("sessionInformationList", srw.getSessionInformationList());
        
        return "index";
    }
}
