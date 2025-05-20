package com.ssafy.pjt.financial.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.entity.Expense;

@Mapper
public interface ExpenseMapper {
	List<Expense> getExpenseListOfGroup(Integer groupId);
	void insertExpense(AddExpenseRequestDto addExpenseRequest);
	void deleteExpense(Integer expenseId);
}
