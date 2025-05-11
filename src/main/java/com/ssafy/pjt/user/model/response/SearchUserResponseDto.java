package com.ssafy.pjt.user.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchUserResponseDto {
	private String id;
	private String nickname;
}
