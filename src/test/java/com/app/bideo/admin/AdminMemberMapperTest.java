package com.app.bideo.admin;

import com.app.bideo.dto.admin.AdminMemberDetailResponseDTO;
import com.app.bideo.dto.admin.AdminMemberListResponseDTO;
import com.app.bideo.dto.member.MemberSearchDTO;
import com.app.bideo.mapper.admin.AdminMemberMapper;
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
class AdminMemberMapperTest {

    @Autowired
    private AdminMemberMapper adminMemberMapper;

    @Test
    void selectMemberList_returnsAll() {
        MemberSearchDTO searchDTO = new MemberSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);

        List<AdminMemberListResponseDTO> result = adminMemberMapper.selectMemberList(searchDTO);

        assertNotNull(result);
        log.info("member list size: {}", result.size());
    }

    @Test
    void selectMemberList_filterByKeyword() {
        MemberSearchDTO searchDTO = new MemberSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);
        searchDTO.setKeyword("test");

        List<AdminMemberListResponseDTO> result = adminMemberMapper.selectMemberList(searchDTO);

        assertNotNull(result);
        log.info("filtered member list size: {}", result.size());
    }

    @Test
    void selectMemberList_filterByStatus() {
        MemberSearchDTO searchDTO = new MemberSearchDTO();
        searchDTO.setPage(1);
        searchDTO.setSize(20);
        searchDTO.setStatus("ACTIVE");

        List<AdminMemberListResponseDTO> result = adminMemberMapper.selectMemberList(searchDTO);

        assertNotNull(result);
        result.forEach(member -> assertEquals("ACTIVE", member.getStatus()));
    }

    @Test
    void selectMemberDetail_nonExistentId_returnsNull() {
        AdminMemberDetailResponseDTO result = adminMemberMapper.selectMemberDetail(999999L);

        assertNull(result);
    }

    @Test
    void countMembers_returnsCount() {
        MemberSearchDTO searchDTO = new MemberSearchDTO();

        int count = adminMemberMapper.countMembers(searchDTO);

        assertTrue(count >= 0);
        log.info("total member count: {}", count);
    }
}
