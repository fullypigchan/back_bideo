package com.app.bideo.admin;

import com.app.bideo.dto.admin.ReportResponseDTO;
import com.app.bideo.dto.admin.ReportSearchDTO;
import com.app.bideo.mapper.admin.AdminReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
@Transactional
class AdminReportMapperTest {

    @Autowired
    private AdminReportMapper adminReportMapper;

    @Test
    void selectReportList_returnsAll() {
        ReportSearchDTO searchDTO = new ReportSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);

        List<ReportResponseDTO> result = adminReportMapper.selectReportList(searchDTO);

        assertNotNull(result);
        log.info("report list size: {}", result.size());
    }

    @Test
    void selectReportList_filterByTargetType() {
        ReportSearchDTO searchDTO = new ReportSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);
        searchDTO.setTargetType("WORK");

        List<ReportResponseDTO> result = adminReportMapper.selectReportList(searchDTO);

        assertNotNull(result);
        result.forEach(report -> assertEquals("WORK", report.getTargetType()));
    }

    @Test
    void selectReportList_filterByStatus() {
        ReportSearchDTO searchDTO = new ReportSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);
        searchDTO.setStatus("PENDING");

        List<ReportResponseDTO> result = adminReportMapper.selectReportList(searchDTO);

        assertNotNull(result);
        result.forEach(report -> assertEquals("PENDING", report.getStatus()));
    }

    @Test
    void selectReportDetail_nonExistentId_returnsNull() {
        ReportResponseDTO result = adminReportMapper.selectReportDetail(999999L);

        assertNull(result);
    }

    @Test
    void countReports_returnsCount() {
        ReportSearchDTO searchDTO = new ReportSearchDTO();

        int count = adminReportMapper.countReports(searchDTO);

        assertTrue(count >= 0);
        log.info("total report count: {}", count);
    }
}
