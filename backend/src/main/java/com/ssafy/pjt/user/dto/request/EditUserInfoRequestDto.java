package com.ssafy.pjt.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditUserInfoRequestDto {
	
	@Schema(description="사용자 ID (서버가 생성함)", accessMode=Schema.AccessMode.READ_ONLY)
	private String id;
	
	@Schema(description="수정할 새로운 닉네임")
	private String newNickname;
	
	@Schema(description="수정할 새로운 나이")
	private String newAge;
}
