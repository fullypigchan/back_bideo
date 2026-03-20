package com.app.bideo.mapper.member;

import com.app.bideo.domain.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    // 회원 정보를 등록
    void insert(MemberVO memberVO);

    // 회원 번호로 회원 정보를 조회
    Optional<MemberVO> selectById(Long id);

    // 이메일로 회원 정보를 조회
    Optional<MemberVO> selectByEmail(String email);

    // 전화번호로 회원 정보를 조회
    Optional<MemberVO> selectByPhoneNumber(String phoneNumber);

    // 이메일 중복 여부를 조회
    boolean existsByEmail(String email);

    // 닉네임 중복 여부를 조회
    boolean existsByNickname(String nickname);

    // 마지막 로그인 시간을 수정
    void updateLastLogin(Long memberId);

    // 이메일 기준 비밀번호를 수정
    void updatePasswordByEmail(String email, String password);
}
