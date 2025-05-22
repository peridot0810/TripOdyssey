package com.ssafy.pjt.financial.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddExpenseRequestDto {
	private String datetime;
	private String description;
	private Integer amount;
	private Integer categoryId;
	private Integer contentId;
	private Integer groupId;
	
	@Schema(description="새로 생성된 가계부 항목 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer expenseId;
}
