package com.ssafy.pjt.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpRequestDto {
	
	@Schema(description="회원가입할 아이디")
	private String id;
	
	@Schema(description="회원가입할 이메일")
	private String email;
	
	@Schema(description="비밀번호")
	private String pw;
	
	@Schema(description="닉네임")
	private String nickname;
	
	@Schema(description="성별")
	private String gender;
	
	@Schema(description="나이")
	private Integer age;
}
