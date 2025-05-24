package com.ssafy.pjt.financial.dto.response;

import lombok.Data;

@Data
public class MemberExpenseInfoResponseDto {
	private String userId;
	private Integer expenseToPay;
	private Integer paidAmount;
}
