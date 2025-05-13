package com.ssafy.pjt.user.dto.request;

import lombok.Data;

@Data
public class SignUpRequestDto {
	private String id;
	private String email;
	private String pw;
	private String nickname;
	private String gender;
	private Integer age;
}
