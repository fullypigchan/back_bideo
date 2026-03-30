package com.app.bideo.admin;

import com.app.bideo.dto.admin.InquiryResponseDTO;
import com.app.bideo.dto.admin.InquirySearchDTO;
import com.app.bideo.repository.admin.AdminInquiryDAO;
import com.app.bideo.service.admin.AdminInquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class AdminInquiryServiceTest {

    private AdminInquiryDAO adminInquiryDAO;
    private AdminInquiryService adminInquiryService;

    @BeforeEach
    void setUp() {
        adminInquiryDAO = Mockito.mock(AdminInquiryDAO.class);
        adminInquiryService = new AdminInquiryService(adminInquiryDAO);
    }

    @Test
    void getInquiries_returnsList() {
        InquirySearchDTO searchDTO = new InquirySearchDTO();
        InquiryResponseDTO dto = InquiryResponseDTO.builder()
                .id(1L)
                .status("PENDING")
                .build();
        given(adminInquiryDAO.findAll(searchDTO)).willReturn(List.of(dto));

        List<InquiryResponseDTO> result = adminInquiryService.getInquiries(searchDTO);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getInquiryDetail_existingId_returnsDTO() {
        InquiryResponseDTO dto = InquiryResponseDTO.builder()
                .id(1L)
                .content("테스트 문의")
                .build();
        given(adminInquiryDAO.findById(1L)).willReturn(Optional.of(dto));

        InquiryResponseDTO result = adminInquiryService.getInquiryDetail(1L);

        assertEquals("테스트 문의", result.getContent());
    }

    @Test
    void getInquiryDetail_nonExistentId_throwsException() {
        given(adminInquiryDAO.findById(999L)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                adminInquiryService.getInquiryDetail(999L)
        );
    }

    @Test
    void replyInquiry_validReply_callsDAO() {
        adminInquiryService.replyInquiry(1L, "답변 내용입니다.");

        verify(adminInquiryDAO).updateReply(1L, "답변 내용입니다.");
    }

    @Test
    void replyInquiry_nullReply_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminInquiryService.replyInquiry(1L, null)
        );
    }

    @Test
    void replyInquiry_emptyReply_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminInquiryService.replyInquiry(1L, "   ")
        );
    }
}
