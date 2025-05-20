package com.ssafy.pjt.financial.service;

import java.util.List;

import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;

public interface ExpenseTrackerService {

	List<ExpenseInfoResponseDto> getExpenseTracker(String userId, Integer groupId);
	Integer addExpense(String userId, Integer groupId, AddExpenseRequestDto expense);
	void deleteExpense(String userId, Integer groupId, Integer expenseId);
}
