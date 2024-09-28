package com.wasd.common.controller;

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

    @GetMapping("/user/login")
    public String loginPage(){
        return "/pages/user/login";
    }
}
