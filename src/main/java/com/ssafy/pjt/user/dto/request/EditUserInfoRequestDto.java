package com.ssafy.pjt.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditUserInfoRequestDto {
	private String id;
	private String newNickname;
	private String newAge;
}
