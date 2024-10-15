package com.wasd.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final Map<Integer, String> errorMsg = new HashMap<>();

    public CustomErrorController() {
        errorMsg.put(404, "요청하신 페이지가 사라졌거나, 잘못된 경로입니다.");
        errorMsg.put(500, "예기치 않은 오류가 발생하여 요청을 처리할 수 없습니다.");
        errorMsg.put(403, "접근이 금지된 페이지입니다. 필요한 권한을 확인해 주세요.");
        errorMsg.put(400, "전송한 요청에 문제가 있어 페이지를 불러올 수 없습니다.");
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code"); // HTTP 상태 코드
        model.addAttribute("errorMsg", errorMsg.getOrDefault(statusCode, "알 수 없는 오류가 발생했습니다."));

        return "/common/error";
    }


}
