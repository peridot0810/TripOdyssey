package com.ssafy.pjt.user.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String email;
	private String pw;
}
