package com.app.bideo.admin;

import com.app.bideo.dto.admin.ReportResponseDTO;
import com.app.bideo.dto.admin.ReportSearchDTO;
import com.app.bideo.repository.admin.AdminReportDAO;
import com.app.bideo.service.admin.AdminReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class AdminReportServiceTest {

    private AdminReportDAO adminReportDAO;
    private AdminReportService adminReportService;

    @BeforeEach
    void setUp() {
        adminReportDAO = Mockito.mock(AdminReportDAO.class);
        adminReportService = new AdminReportService(adminReportDAO);
    }

    @Test
    void getReports_returnsList() {
        ReportSearchDTO searchDTO = new ReportSearchDTO();
        ReportResponseDTO dto = ReportResponseDTO.builder()
                .id(1L)
                .status("PENDING")
                .build();
        given(adminReportDAO.findAll(searchDTO)).willReturn(List.of(dto));

        List<ReportResponseDTO> result = adminReportService.getReports(searchDTO);

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    void getReportDetail_existingId_returnsDTO() {
        ReportResponseDTO dto = ReportResponseDTO.builder()
                .id(1L)
                .targetType("WORK")
                .reason("COPYRIGHT")
                .build();
        given(adminReportDAO.findById(1L)).willReturn(Optional.of(dto));

        ReportResponseDTO result = adminReportService.getReportDetail(1L);

        assertEquals("WORK", result.getTargetType());
        assertEquals("COPYRIGHT", result.getReason());
    }

    @Test
    void getReportDetail_nonExistentId_throwsException() {
        given(adminReportDAO.findById(999L)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                adminReportService.getReportDetail(999L)
        );
    }

    @Test
    void updateReportStatus_pending_callsDAO() {
        adminReportService.updateReportStatus(1L, "PENDING");

        verify(adminReportDAO).updateStatus(1L, "PENDING");
    }

    @Test
    void updateReportStatus_reviewing_callsDAO() {
        adminReportService.updateReportStatus(1L, "REVIEWING");

        verify(adminReportDAO).updateStatus(1L, "REVIEWING");
    }

    @Test
    void updateReportStatus_resolved_callsDAO() {
        adminReportService.updateReportStatus(1L, "RESOLVED");

        verify(adminReportDAO).updateStatus(1L, "RESOLVED");
    }

    @Test
    void updateReportStatus_cancelled_callsDAO() {
        adminReportService.updateReportStatus(1L, "CANCELLED");

        verify(adminReportDAO).updateStatus(1L, "CANCELLED");
    }

    @Test
    void updateReportStatus_invalidStatus_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminReportService.updateReportStatus(1L, "INVALID")
        );
    }

    @Test
    void updateReportStatus_nullStatus_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminReportService.updateReportStatus(1L, null)
        );
    }
}
