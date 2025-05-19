package com.ssafy.pjt.financial.dto.request;

import lombok.Data;

@Data
public class UpdateFeeRequestDto {
	private String userId;
	private Integer groupId;
	private Integer newFee;
}
