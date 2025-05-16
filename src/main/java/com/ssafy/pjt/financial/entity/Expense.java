package com.ssafy.pjt.financial.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Expense {
	private Integer expenseId;
	private Integer groupId;
	private Integer contentId;
	private Integer categoryId;
	private String contentName;
	private String categoryName;
	private String datetime;
	private String description;
	private Integer amount;
}

