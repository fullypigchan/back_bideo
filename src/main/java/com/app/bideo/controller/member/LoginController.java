package com.app.bideo.controller.member;

import com.app.bideo.auth.member.CustomUserDetails;
import com.app.bideo.auth.member.JwtTokenProvider;
import com.app.bideo.domain.member.MemberVO;
import com.app.bideo.dto.member.MemberLoginRequestDTO;
import com.app.bideo.repository.member.MemberRepository;
import com.app.bideo.service.member.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // 로그인 처리 및 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO requestDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        MemberVO memberVO = memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        authService.updateLastLogin(memberVO.getId());
        String accessToken = jwtTokenProvider.createAccessToken(memberVO.getEmail(), "LOCAL", response);
        jwtTokenProvider.createRefreshToken(memberVO.getEmail(), "LOCAL", response);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "email", memberVO.getEmail(),
                "nickname", memberVO.getNickname()
        ));
    }

    // 로그아웃 처리 및 토큰 무효화
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        if (jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmail(accessToken);
            jwtTokenProvider.deleteRefreshToken(email);
            jwtTokenProvider.addToBlacklist(accessToken);
        }
        jwtTokenProvider.clearTokenCookies(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "로그아웃이 완료되었습니다."));
    }

    // 현재 로그인한 회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        String email = jwtTokenProvider.getEmail(accessToken);
        return memberRepository.findByEmail(email)
                .map(member -> ResponseEntity.ok(Map.of(
                        "id", member.getId(),
                        "email", member.getEmail(),
                        "nickname", member.getNickname(),
                        "profileImage", member.getProfileImage() == null ? "" : member.getProfileImage()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "회원이 없습니다.")));
    }
}
