package com.wasd.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    @GetMapping("/user/login")
    public ModelAndView login(ModelAndView model) {
        model.addObject("data", "test");
        model.setViewName("/pages/user/login");
        return model;
    }
}
