package com.app.bideo.repository.member;

import com.app.bideo.domain.member.MemberVO;
import com.app.bideo.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberMapper memberMapper;

    // 회원 정보를 저장
    public void save(MemberVO memberVO) {
        memberMapper.insert(memberVO);
    }

    // 회원 번호로 회원 정보를 조회
    public Optional<MemberVO> findById(Long id) {
        return memberMapper.selectById(id);
    }

    // 이메일로 회원 정보를 조회
    public Optional<MemberVO> findByEmail(String email) {
        return memberMapper.selectByEmail(email);
    }

    // 전화번호로 회원 정보를 조회
    public Optional<MemberVO> findByPhoneNumber(String phoneNumber) {
        return memberMapper.selectByPhoneNumber(phoneNumber);
    }

    // 이메일 중복 여부를 확인
    public boolean existsByEmail(String email) {
        return memberMapper.existsByEmail(email);
    }

    // 닉네임 중복 여부를 확인
    public boolean existsByNickname(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }

    // 마지막 로그인 시간을 수정
    public void updateLastLogin(Long memberId) {
        memberMapper.updateLastLogin(memberId);
    }

    // 이메일 기준 비밀번호를 수정
    public void updatePasswordByEmail(String email, String password) {
        memberMapper.updatePasswordByEmail(email, password);
    }
}
