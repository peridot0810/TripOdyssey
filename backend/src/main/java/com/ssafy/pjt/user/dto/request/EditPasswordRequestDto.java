package com.ssafy.pjt.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditPasswordRequestDto {
	
	@Schema(description="사용자 ID (서버가 생성함)", accessMode=Schema.AccessMode.READ_ONLY)
	private String id;
	
	@Schema(description="사용자의 현재 비밀번호")
	@NotNull(message = "현재 비밀번호는 필수 입력값입니다.")
	private String currentPassword;
	
	@Schema(description="변경될 새로운 비밀번호")
	@NotNull(message = "새로운 비밀번호는 필수 입력값입니다.")
	private String newPassword;
}
