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
    private final Map<Integer, String> errorMsgSub = new HashMap<>();

    public CustomErrorController() {
        errorMsg.put(404, "죄송합니다. 현재 찾을 수 없는 페이지를 요청하셨습니다.");
        errorMsgSub.put(404, "페이지의 주소가 잘못 입력되었거나, 주소 변경 및 삭제되어 요청하신 페이지를 찾을 수 없습니다.");

        errorMsg.put(500, "죄송합니다. 서버에서 문제가 발생했습니다.");
        errorMsgSub.put(500, "예기치 않은 오류가 발생하여 요청을 처리할 수 없습니다.\n잠시 후 다시 시도해 주세요.");

        errorMsg.put(403, "죄송합니다. 접근이 금지된 페이지입니다.");
        errorMsgSub.put(403, "이 페이지에 접근할 권한이 없습니다.\n필요한 권한을 확인하시기 바랍니다.");

        errorMsg.put(400, "죄송합니다. 잘못된 요청입니다.");
        errorMsgSub.put(400, "전송한 요청에 문제가 있어 페이지를 불러올 수 없습니다. \n잠시 후 다시 시도해 주세요.");
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code"); // HTTP 상태 코드
        model.addAttribute("status", statusCode);
        model.addAttribute("errorMsg", errorMsg.getOrDefault(statusCode, "알 수 없는 오류가 발생했습니다."));
        model.addAttribute("errorMsgSub", errorMsgSub.getOrDefault(statusCode, "현재 요청을 처리하는 도중에 문제가 발생했습니다. 잠시 후 다시 시도해 주세요."));

        return "/common/error";
    }


}
