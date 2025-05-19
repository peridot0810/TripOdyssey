package com.ssafy.pjt.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
	private String id;
	private String email;
	private String gender;
	private String nickname;
	private Integer age;
}
