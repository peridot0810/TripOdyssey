package com.ssafy.pjt.financial.repository;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.entity.Expense;

public interface ExpenseRepository {

	List<Expense> getExpenseListOfGroup(Integer groupId);
	void insertExpense(AddExpenseRequestDto addExpenseRequest);
	void deleteExpense(Integer expenseId);
	
	// 공통 기능 
	Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest);
	List<Integer> getUserRole(UserGroupRequestDto userRoleRequest);
}
