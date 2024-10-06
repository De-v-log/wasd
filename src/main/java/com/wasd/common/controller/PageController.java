package com.wasd.common.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String sessionControl(HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/login";
        }else{
            // board 로 리다이렉트 예정
            return "redirect:/login";
        }
    }


    /**
     *
     * @return
     */
    @GetMapping("/login")
    public String loginPage(){
        return "/pages/user/login";
    }

    @GetMapping("/user/profileSetting")
    public String profileSetting(){
        return "/pages/user/profileSetting";

    }
}
