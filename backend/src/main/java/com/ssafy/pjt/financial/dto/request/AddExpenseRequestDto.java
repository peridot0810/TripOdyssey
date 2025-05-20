package com.ssafy.pjt.financial.dto.request;

import lombok.Data;

@Data
public class AddExpenseRequestDto {
	private String datetime;
	private String description;
	private Integer amount;
	private Integer categoryId;
	private Integer contentId;
	private Integer groupId;
	private Integer expenseId;
}
