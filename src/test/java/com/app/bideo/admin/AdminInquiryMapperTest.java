package com.app.bideo.admin;

import com.app.bideo.dto.admin.InquiryResponseDTO;
import com.app.bideo.dto.admin.InquirySearchDTO;
import com.app.bideo.mapper.admin.AdminInquiryMapper;
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
class AdminInquiryMapperTest {

    @Autowired
    private AdminInquiryMapper adminInquiryMapper;

    @Test
    void selectInquiryList_returnsAll() {
        InquirySearchDTO searchDTO = new InquirySearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);

        List<InquiryResponseDTO> result = adminInquiryMapper.selectInquiryList(searchDTO);

        assertNotNull(result);
        log.info("inquiry list size: {}", result.size());
    }

    @Test
    void selectInquiryList_filterByStatus() {
        InquirySearchDTO searchDTO = new InquirySearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);
        searchDTO.setStatus("PENDING");

        List<InquiryResponseDTO> result = adminInquiryMapper.selectInquiryList(searchDTO);

        assertNotNull(result);
        result.forEach(inquiry -> assertEquals("PENDING", inquiry.getStatus()));
    }

    @Test
    void countInquiries_returnsCount() {
        InquirySearchDTO searchDTO = new InquirySearchDTO();

        int count = adminInquiryMapper.countInquiries(searchDTO);

        assertTrue(count >= 0);
        log.info("total inquiry count: {}", count);
    }

    @Test
    void selectInquiryDetail_nonExistentId_returnsNull() {
        InquiryResponseDTO result = adminInquiryMapper.selectInquiryDetail(999999L);

        assertNull(result);
    }
}
