package com.app.bideo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping("/members")
    public String memberList() {
        return "admin/admin-member-list";
    }

    @GetMapping("/members/{id}")
    public String memberDetail(@PathVariable Long id) {
        return "admin/admin-member-detail";
    }

    @GetMapping("/inquiries")
    public String inquiryList() {
        return "admin/admin-inquiry-list";
    }

    @GetMapping("/inquiries/{id}")
    public String inquiryDetail(@PathVariable Long id) {
        return "admin/admin-inquiry-detail";
    }

    @GetMapping("/reports")
    public String reportList() {
        return "admin/admin-block-list";
    }
}
