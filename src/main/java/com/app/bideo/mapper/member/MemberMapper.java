package com.app.bideo.mapper.member;

import com.app.bideo.domain.member.MemberVO;
import com.app.bideo.dto.member.MemberListResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    void insert(MemberVO memberVO);

    Optional<MemberVO> selectById(Long id);

    Optional<MemberVO> selectByEmail(String email);

    Optional<MemberVO> selectByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);

    void updateLastLogin(Long memberId);

    void updatePassword(@Param("memberId") Long memberId, @Param("password") String password);

    List<MemberListResponseDTO> searchByKeyword(@Param("keyword") String keyword,
                                                @Param("currentMemberId") Long currentMemberId,
                                                @Param("limit") int limit);
}
