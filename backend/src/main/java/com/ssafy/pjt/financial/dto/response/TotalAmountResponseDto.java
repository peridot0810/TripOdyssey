package com.ssafy.pjt.financial.dto.response;

import lombok.Data;

@Data
public class TotalAmountResponseDto {
	private Integer goalAmount;		// 목표 금액
	private Integer raisedAmount;	// 실제 모인 금
}
