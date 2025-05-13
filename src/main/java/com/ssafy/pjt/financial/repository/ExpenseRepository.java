package com.ssafy.pjt.financial.repository;

import java.util.List;
import java.util.Map;

import com.ssafy.pjt.financial.dto.request.CheckUserInGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.UserRoleRequestDto;
import com.ssafy.pjt.financial.entity.Expense;

public interface ExpenseRepository {

	List<Expense> getExpenseListOfGroup(Integer groupId);
	Boolean checkUserInGroup(CheckUserInGroupRequestDto checkUserInGroupRequest);
	void insertExpense(Map<String, Object> expense);
	String getUserRole(UserRoleRequestDto userRoleRequest);
}
