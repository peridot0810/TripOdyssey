package com.ssafy.pjt.financial.dto.request;

import lombok.Data;

@Data
public class AddExpenseRequestWrapperDto {
	private Integer groupId;
	private AddExpenseRequestDto newExpense;
}
