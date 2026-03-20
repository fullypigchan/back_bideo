package com.app.bideo.service.member;

import com.app.bideo.common.enumeration.MemberRole;
import com.app.bideo.common.enumeration.MemberStatus;
import com.app.bideo.domain.member.MemberVO;
import com.app.bideo.dto.member.MemberSignupRequestDTO;
import com.app.bideo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberVO signup(MemberSignupRequestDTO requestDTO) {
        String email = normalizeEmail(requestDTO.getEmail());
        String loginId = normalizeText(requestDTO.getLoginId(), "아이디를 입력하세요.");
        String password = normalizeText(requestDTO.getPassword(), "비밀번호를 입력하세요.");
        String nickname = normalizeText(requestDTO.getNickname(), "닉네임을 입력하세요.");

        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        MemberVO memberVO = MemberVO.builder()
                .email(email)
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .realName(requestDTO.getRealName())
                .birthDate(requestDTO.getBirthDate())
                .phoneNumber(normalizePhoneNumber(requestDTO.getPhoneNumber()))
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .creatorVerified(false)
                .sellerVerified(false)
                .creatorTier("BASIC")
                .followerCount(0)
                .followingCount(0)
                .galleryCount(0)
                .build();

        memberRepository.save(memberVO);
        return memberVO;
    }

    @Transactional(readOnly = true)
    public MemberVO findActiveMemberByEmail(String email) {
        return memberRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public MemberVO findActiveMemberByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(normalizePhoneNumber(phoneNumber))
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional
    public void resetPasswordByEmail(String email, String newPassword, String confirmPassword) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedPassword = normalizeText(newPassword, "새 비밀번호를 입력하세요.");
        String normalizedConfirmPassword = normalizeText(confirmPassword, "비밀번호 확인을 입력하세요.");

        if (!normalizedPassword.equals(normalizedConfirmPassword)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }

        findActiveMemberByEmail(normalizedEmail);
        memberRepository.updatePasswordByEmail(normalizedEmail, passwordEncoder.encode(normalizedPassword));
    }

    @Transactional
    public void updateLastLogin(Long memberId) {
        memberRepository.updateLastLogin(memberId);
    }

    private String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("이메일을 입력하세요.");
        }

        return email.trim().toLowerCase();
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new IllegalArgumentException("전화번호를 입력하세요.");
        }

        return phoneNumber.replaceAll("[^0-9]", "");
    }

    private String normalizeText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
