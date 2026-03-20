package com.app.bideo.controller.member;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // 로그인 상태에 따라 메인 화면을 분기
    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "main/main";
        }
        return "main/intro-main";
    }

    // 메인 관련 직접 접근을 루트로 리다이렉트
    @GetMapping({"/main/intro-main", "/main/main"})
    public String redirectMainRoutes() {
        return "redirect:/";
    }

    // 공통 에러 페이지를 반환
    @GetMapping("/error-page")
    public String errorPage() {
        return "error/error";
    }
}
