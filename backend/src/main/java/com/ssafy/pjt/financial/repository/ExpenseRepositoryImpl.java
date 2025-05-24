package com.ssafy.pjt.financial.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.mapper.CommonMapper;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
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
	public Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest) {
		return commonMapper.checkUserInGroup(checkUserInGroupRequest);
	}
	
	@Override
	public void insertExpense(AddExpenseRequestDto addExpenseRequest) {
		expenseMapper.insertExpense(addExpenseRequest);
	}
	
	@Override
	public List<Integer> getUserRole(UserGroupRequestDto userRoleRequest) {
		return commonMapper.getUserRoleInGroup(userRoleRequest);
	}
	
	@Override
	public void deleteExpense(Integer expenseId) {
		expenseMapper.deleteExpense(expenseId);
	}
}
