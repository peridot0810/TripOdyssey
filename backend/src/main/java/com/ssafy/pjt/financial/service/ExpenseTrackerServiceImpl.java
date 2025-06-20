package com.ssafy.pjt.financial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;
import com.ssafy.pjt.financial.entity.Expense;
import com.ssafy.pjt.financial.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExpenseTrackerServiceImpl implements ExpenseTrackerService{

	private final ExpenseRepository expenseRepository;
	private final UserValidationService userValidationService;
	
	@Override
	public List<ExpenseInfoResponseDto> getExpenseTracker(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
	
		// 비즈니스 로직
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
					.contentId(expense.getContentId())
					.build()
					);
		}
		return expenseTracker;
	}
	
	@Override
	public Integer addExpense(String userId, Integer groupId, AddExpenseRequestDto expense) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.FINANCE.getId())) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 가계부 항목을 추가할 수 있습니다.");
		}
		
		// 비즈니스 로직
		expense.setGroupId(groupId);
	    expenseRepository.insertExpense(expense);
	    
	    return expense.getExpenseId();
	}
	
	@Override
	public void deleteExpense(String userId, Integer groupId, Integer expenseId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.FINANCE.getId())) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 가계부 항목을 추가할 수 있습니다.");
		}
		
		// 비즈니스 로직
		expenseRepository.deleteExpense(expenseId);
	}
}
