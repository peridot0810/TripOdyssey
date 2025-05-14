package com.ssafy.pjt.financial.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.common.dto.request.UserRoleInGroupRequestDto;
import com.ssafy.pjt.common.mapper.CommonMapper;
import com.ssafy.pjt.financial.dto.request.CheckUserInGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.UserRoleRequestDto;
import com.ssafy.pjt.financial.entity.Expense;
import com.ssafy.pjt.financial.mapper.ExpenseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository{

	private final ExpenseMapper expenseMapper;
	private final CommonMapper commonMapper;
	
	@Override
	public List<Expense> getExpenseListOfGroup(Integer groupId) {
		return expenseMapper.getExpenseListOfGroup(groupId);
	}
	
	@Override
	public Boolean checkUserInGroup(CheckUserInGroupRequestDto checkUserInGroupRequest) {
		return expenseMapper.checkUserInGroup(checkUserInGroupRequest);
	}
	
	@Override
	public void insertExpense(Map<String, Object> expense) {
		expenseMapper.insertExpense(expense);
	}
	
	@Override
	public Integer getUserRole(UserRoleInGroupRequestDto userRoleRequest) {
		return commonMapper.getUserRoleInGroup(userRoleRequest);
	}
}
