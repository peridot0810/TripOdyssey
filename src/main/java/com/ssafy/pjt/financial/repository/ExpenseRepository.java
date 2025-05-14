package com.ssafy.pjt.financial.repository;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.financial.entity.Expense;

public interface ExpenseRepository {

	List<Expense> getExpenseListOfGroup(Integer groupId);
	Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest);
	void insertExpense(Map<String, Object> expense);
	Integer getUserRole(UserGroupRequestDto userRoleRequest);
}
