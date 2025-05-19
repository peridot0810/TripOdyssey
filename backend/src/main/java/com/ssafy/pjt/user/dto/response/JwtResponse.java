package com.ssafy.pjt.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private LoginResponseDto user;
}
