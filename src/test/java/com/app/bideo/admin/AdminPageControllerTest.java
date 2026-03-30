package com.app.bideo.admin;

import com.app.bideo.controller.admin.AdminPageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminPageControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminPageController()).build();
    }

    @Test
    void adminPagesResolveExpectedTemplates() throws Exception {
        mockMvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-member-list"));

        mockMvc.perform(get("/admin/members/21"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-member-detail"));

        mockMvc.perform(get("/admin/inquiries"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-inquiry-list"));

        mockMvc.perform(get("/admin/inquiries/11"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-inquiry-detail"));

        mockMvc.perform(get("/admin/reports"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-block-list"));
    }
}
