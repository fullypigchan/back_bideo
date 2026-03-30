package com.app.bideo.admin;

import com.app.bideo.controller.admin.AdminInquiryAPIController;
import com.app.bideo.controller.admin.AdminMemberAPIController;
import com.app.bideo.controller.admin.AdminReportAPIController;
import com.app.bideo.dto.admin.AdminMemberDetailResponseDTO;
import com.app.bideo.dto.admin.AdminMemberListResponseDTO;
import com.app.bideo.dto.admin.InquiryResponseDTO;
import com.app.bideo.dto.admin.ReportResponseDTO;
import com.app.bideo.service.admin.AdminInquiryService;
import com.app.bideo.service.admin.AdminMemberService;
import com.app.bideo.service.admin.AdminReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminApiControllerTest {

    private MockMvc mockMvc;
    private AdminInquiryService adminInquiryService;
    private AdminMemberService adminMemberService;
    private AdminReportService adminReportService;

    @BeforeEach
    void setUp() {
        adminInquiryService = Mockito.mock(AdminInquiryService.class);
        adminMemberService = Mockito.mock(AdminMemberService.class);
        adminReportService = Mockito.mock(AdminReportService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new AdminInquiryAPIController(adminInquiryService),
                new AdminMemberAPIController(adminMemberService),
                new AdminReportAPIController(adminReportService)
        ).build();
    }

    @Test
    void adminListAndDetailEndpointsExposeExpectedPayloads() throws Exception {
        given(adminInquiryService.getInquiries(any())).willReturn(List.of(
                InquiryResponseDTO.builder()
                        .id(11L)
                        .memberNickname("member-a")
                        .status("PENDING")
                        .build()
        ));
        given(adminInquiryService.getInquiryDetail(11L)).willReturn(
                InquiryResponseDTO.builder()
                        .id(11L)
                        .content("문의 내용")
                        .reply("답변 완료")
                        .build()
        );
        given(adminMemberService.getMembers(any())).willReturn(List.of(
                AdminMemberListResponseDTO.builder()
                        .id(21L)
                        .email("admin@test.com")
                        .status("ACTIVE")
                        .build()
        ));
        given(adminMemberService.getMemberDetail(21L)).willReturn(
                AdminMemberDetailResponseDTO.builder()
                        .id(21L)
                        .email("admin@test.com")
                        .nickname("admin")
                        .build()
        );
        given(adminReportService.getReports(any())).willReturn(List.of(
                ReportResponseDTO.builder()
                        .id(31L)
                        .targetType("WORK")
                        .status("PENDING")
                        .build()
        ));
        given(adminReportService.getReportDetail(31L)).willReturn(
                ReportResponseDTO.builder()
                        .id(31L)
                        .reason("COPYRIGHT")
                        .status("RESOLVED")
                        .build()
        );

        mockMvc.perform(get("/api/admin/inquiries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(11))
                .andExpect(jsonPath("$[0].memberNickname").value("member-a"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        mockMvc.perform(get("/api/admin/inquiries/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.content").value("문의 내용"))
                .andExpect(jsonPath("$.reply").value("답변 완료"));

        mockMvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(21))
                .andExpect(jsonPath("$[0].email").value("admin@test.com"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        mockMvc.perform(get("/api/admin/members/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.nickname").value("admin"));

        mockMvc.perform(get("/api/admin/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(31))
                .andExpect(jsonPath("$[0].targetType").value("WORK"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        mockMvc.perform(get("/api/admin/reports/31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(31))
                .andExpect(jsonPath("$.reason").value("COPYRIGHT"))
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void adminMutationEndpointsReturnOk() throws Exception {
        mockMvc.perform(patch("/api/admin/inquiries/11/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"reply":"답변 내용"}
                                """))
                .andExpect(status().isOk());
        verify(adminInquiryService).replyInquiry(11L, "답변 내용");

        mockMvc.perform(patch("/api/admin/members/21/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"SUSPENDED"}
                                """))
                .andExpect(status().isOk());
        verify(adminMemberService).updateMemberStatus(21L, "SUSPENDED");

        mockMvc.perform(patch("/api/admin/reports/31/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"RESOLVED"}
                                """))
                .andExpect(status().isOk());
        verify(adminReportService).updateReportStatus(31L, "RESOLVED");
    }
}
