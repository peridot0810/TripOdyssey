package com.ssafy.pjt.financial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.UserRoleInGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.dto.request.CheckUserInGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.UserRoleRequestDto;
import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;
import com.ssafy.pjt.financial.entity.Expense;
import com.ssafy.pjt.financial.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.financial.exception.UserNotInGroupException;
import com.ssafy.pjt.financial.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExpenseTrackerServiceImpl implements ExpenseTrackerService{

	private final ExpenseRepository expenseRepository;
	
	@Override
	public List<ExpenseInfoResponseDto> getExpenseTracker(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!expenseRepository.checkUserInGroup(CheckUserInGroupRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.build())
				) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
	
		List<Expense> expenseListOfGroup = expenseRepository.getExpenseListOfGroup(groupId);
		List<ExpenseInfoResponseDto> expenseTracker = new ArrayList<>();
		for(Expense expense : expenseListOfGroup) {
			expenseTracker.add(ExpenseInfoResponseDto.builder()
					.expenseId(expense.getExpenseId())
					.datetime(expense.getDatetime())
					.description(expense.getDescription())
					.amount(expense.getAmount())
					.categoryName(expense.getCategoryName())
					.contentName(expense.getContentName())
					.build()
					);
		}
		return expenseTracker;
	}
	
	@Override
	public void addExpense(String userId, Integer groupId, AddExpenseRequestDto expense) {
		// 유저 역할 확인 
		Integer userRole = expenseRepository.getUserRole(new UserRoleInGroupRequestDto(userId, groupId));
		if(userRole != 3) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 가계부 항목을 추가할 수 있습니다.");
		}
		
		// 평탄화 
		Map<String, Object> param = new HashMap<>();
	    param.put("groupId", groupId);
	    param.put("userId", userId);
	    param.put("datetime", expense.getDatetime());
	    param.put("description", expense.getDescription());
	    param.put("amount", expense.getAmount());
	    param.put("categoryId", expense.getCategoryId());  // 주의: DB 컬럼 이름과 맞추기
	    param.put("contentId", expense.getContentId());
	    
	    expenseRepository.insertExpense(param);
	}
}
