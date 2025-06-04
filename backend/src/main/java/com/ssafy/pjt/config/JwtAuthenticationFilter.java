package com.ssafy.pjt.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.pjt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ssafy.pjt.user.service.CustomUserDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;

    // 멤버 변수로 한 번만 생성 (불변 배열)
    private static final String[] EXCLUDE_URLS = {
        "/auth/login",
        "/auth/signup",
        "/ai/",
        "/user/check/",
        "/v3/api-docs/",
        "/swagger-ui/",
        "/swagger-ui.html",
        "/favicon.ico",
        "/uploads/profile/",
        "/uploads/group/"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 요청 URI가 예외 경로에 해당하면 필터 건너뛰기
        for (String excludeUrl : EXCLUDE_URLS) {
            if (path.startsWith(excludeUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            String userId = null;
            try {
                userId = jwtUtil.extractUserId(token);
            } catch (Exception e) {
                log.error("JWT 토큰에서 userId 추출 실패: {}", e.getMessage());
                // 토큰 문제 시 403 응답으로 처리하려면 아래 주석 해제 후 return;
                // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token");
                // return;
            }

            log.debug("userId :{}", userId);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailService.loadUserByUserId(userId);

                if (jwtUtil.validateToken(token, userDetails)) {
                    log.debug("userDetails : {}", userDetails.toString());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
