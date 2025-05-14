package com.ssafy.pjt.common.dto.request;

import lombok.Data;

@Data
public class PayAmountRequestDto {
	private String userId;
	private Integer groupId;
	private Integer paidAmount;
}
