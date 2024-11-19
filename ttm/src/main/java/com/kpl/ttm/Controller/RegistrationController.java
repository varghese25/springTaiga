package com.kpl.ttm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.kpl.ttm.Model.MyAppUser;
import com.kpl.ttm.Model.MyAppUserRepository;

@RestController
public class RegistrationController { /* Code To handle */

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Allowed Multiple Content Types both JSON and application/x-www-form-urlencoded data. It started Post UserDetails in Postgre Database.."application/x-www-form-urlencoded" Enable SignUpage */
    @PostMapping(value = "/req/signup", consumes = {"application/json", "application/x-www-form-urlencoded"})
   
    /*SigNup Page Redirect to Index Page 19-11-2024*/
    public RedirectView createUser(MyAppUser user) { 
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        myAppUserRepository.save(user);

        // Redirect to the index page after successful signup
        return new RedirectView("/index");
        
    }
}