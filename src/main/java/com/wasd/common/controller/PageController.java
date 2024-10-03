package com.wasd.common.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @PackageName : com.wasd.common.controller
 * @FileName : PageController
 * @Date : 9/27/24
 * @Author : y00jin
 * @Description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 9/27/24        y00jin       최초 생성
 */
@Controller
public class PageController {
    @GetMapping("/")
    public String sessionControl(HttpSession session){
        if (session.getAttribute("user") == null){
            return "redirect:/user/login";
        }else{
            // board 로 리다이렉트 예정
            return "redirect:/user/login";
        }
    }

    @GetMapping("/user/login")
    public String loginPage(){
        return "/pages/user/login";
    }
}
