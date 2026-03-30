package com.app.bideo.admin;

import com.app.bideo.dto.admin.AdminMemberDetailResponseDTO;
import com.app.bideo.dto.admin.AdminMemberListResponseDTO;
import com.app.bideo.dto.member.MemberSearchDTO;
import com.app.bideo.repository.admin.AdminMemberDAO;
import com.app.bideo.service.admin.AdminMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class AdminMemberServiceTest {

    private AdminMemberDAO adminMemberDAO;
    private AdminMemberService adminMemberService;

    @BeforeEach
    void setUp() {
        adminMemberDAO = Mockito.mock(AdminMemberDAO.class);
        adminMemberService = new AdminMemberService(adminMemberDAO);
    }

    @Test
    void getMembers_returnsList() {
        MemberSearchDTO searchDTO = new MemberSearchDTO();
        AdminMemberListResponseDTO dto = AdminMemberListResponseDTO.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("tester")
                .status("ACTIVE")
                .build();
        given(adminMemberDAO.findAll(searchDTO)).willReturn(List.of(dto));

        List<AdminMemberListResponseDTO> result = adminMemberService.getMembers(searchDTO);

        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }

    @Test
    void getMemberDetail_existingId_returnsDTO() {
        AdminMemberDetailResponseDTO dto = AdminMemberDetailResponseDTO.builder()
                .id(1L)
                .email("test@example.com")
                .build();
        given(adminMemberDAO.findById(1L)).willReturn(Optional.of(dto));

        AdminMemberDetailResponseDTO result = adminMemberService.getMemberDetail(1L);

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getMemberDetail_nonExistentId_throwsException() {
        given(adminMemberDAO.findById(999L)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                adminMemberService.getMemberDetail(999L)
        );
    }

    @Test
    void updateMemberStatus_validStatus_callsDAO() {
        adminMemberService.updateMemberStatus(1L, "SUSPENDED");

        verify(adminMemberDAO).updateStatus(1L, "SUSPENDED");
    }

    @Test
    void updateMemberStatus_active_callsDAO() {
        adminMemberService.updateMemberStatus(1L, "ACTIVE");

        verify(adminMemberDAO).updateStatus(1L, "ACTIVE");
    }

    @Test
    void updateMemberStatus_banned_callsDAO() {
        adminMemberService.updateMemberStatus(1L, "BANNED");

        verify(adminMemberDAO).updateStatus(1L, "BANNED");
    }

    @Test
    void updateMemberStatus_invalidStatus_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminMemberService.updateMemberStatus(1L, "INVALID")
        );
    }

    @Test
    void updateMemberStatus_nullStatus_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                adminMemberService.updateMemberStatus(1L, null)
        );
    }
}
