package com.ssafy.pjt.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDto {
	
	@Schema(description="로그인할 계정의 이메일")
	private String email;
	
	@Schema(description="로그인할 계정의 비밀번호")
	private String pw;
}
