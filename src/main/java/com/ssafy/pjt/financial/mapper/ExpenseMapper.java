package com.ssafy.pjt.financial.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.financial.entity.Expense;

@Mapper
public interface ExpenseMapper {
	List<Expense> getExpenseListOfGroup(Integer groupId);
	void insertExpense(Map<String, Object> expense);
}
