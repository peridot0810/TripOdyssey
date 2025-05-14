package com.ssafy.pjt.financial.service;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.financial.exception.UserNotInGroupException;
import com.ssafy.pjt.financial.repository.ExpenseRepository;
import com.ssafy.pjt.financial.repository.FinancialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService{

	private final FinancialRepository financialRepository;
	private final ExpenseRepository expenseRepository;
	
	
	@Override
	public void setFee(String userId, Integer groupId, SetFeeRequestDto setFeeRequest) {
		
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!expenseRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		Integer userRole = expenseRepository.getUserRole(new UserGroupRequestDto(userId, groupId));
		if(userRole != 3) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 회비를 설정할 수 있습니다.");
		}
		
		setFeeRequest.setGroupId(groupId);
		financialRepository.setFee(setFeeRequest);
	}
}
