package com.ssafy.pjt.user.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.user.entity.CustomUserDetails;
import com.ssafy.pjt.user.entity.User;
import com.ssafy.pjt.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

	private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        // UserDetails를 반환해야 함
        return CustomUserDetails.builder()
                .username(user.getId())			// 아이디
                .password(user.getPw()) 		// 암호화된 비밀번호
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                .nickname(user.getNickname())	// 닉네임
                .email(user.getEmail())			// 이메일
                .build();
    }
    
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // UserDetails를 반환해야 함
        return CustomUserDetails.builder()
                .username(user.getId())
                .password(user.getPw()) // 암호화된 비밀번호
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                .build();
    }
}
