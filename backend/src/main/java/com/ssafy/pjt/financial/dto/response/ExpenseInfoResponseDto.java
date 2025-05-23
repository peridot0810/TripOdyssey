package com.ssafy.pjt.financial.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseInfoResponseDto {
	private Integer expenseId;
	private String datetime;
	private String description;
	private Integer amount;
	private String categoryName;
	private String contentName;
	private Integer contentId;
}
