package com.kpl.ttm.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContentController {

   @GetMapping("/login")
    public String showIndex() {
        return "loginPage";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signupPage";
    }

}