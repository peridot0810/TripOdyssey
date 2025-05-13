package com.ssafy.pjt.financial.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckUserInGroupRequestDto {
	private String userId;
	private Integer groupId;
}
