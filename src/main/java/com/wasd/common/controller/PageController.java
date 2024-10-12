package com.wasd.common.controller;

import com.wasd.config.security.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @GetMapping("/login")
    public String loginPage(){
        return "/pages/user/login";
    }

    @GetMapping("/join")
    public String joinPage(Model model, @AuthenticationPrincipal CustomOAuth2User oAuth2User){
        model.addAttribute("oauth", oAuth2User.getUserInfo());
        return "/pages/user/join";
    }

    @GetMapping("/main")
    public String mainPage(){
        return "/pages/main/main";
    }

    @GetMapping("/main/profileSetting")
    public String mainProfileSettingPage(){
        return "/pages/main/profileSetting";
    }

    @GetMapping("/main/group")
    public String mainGroupPage(){
        return "/pages/main/group";
    }

    @GetMapping("/main/group/{groupId}")
    public String mainGroupDetailPage(@PathVariable Long groupId){
        return "/pages/main/groupDetail";
    }
}
