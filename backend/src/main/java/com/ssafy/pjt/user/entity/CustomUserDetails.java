package com.ssafy.pjt.user.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 4277680938787525561L;
	
	private String username;
    private String password;
    private String nickname;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    // UserDetails 구현 메서드들
    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return true; }
}
