package com.ssafy.pjt.financial.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.MemberExpenseInfoResponseDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;
import com.ssafy.pjt.financial.repository.ExpenseRepository;
import com.ssafy.pjt.financial.repository.FinancialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService{

	private final FinancialRepository financialRepository;
	private final UserValidationService userValidationService;
	
	
	@Override
	public void setFee(String userId, Integer groupId, SetFeeRequestDto setFeeRequest) {
		
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.FINANCE.getId())) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 회비를 설정할 수 있습니다.");
		}
		
		// 비즈니스 로직
		setFeeRequest.setGroupId(groupId);
		financialRepository.setFee(setFeeRequest);
	}
	
	@Override
	public TotalAmountResponseDto getTotalAmount(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		return financialRepository.getTotalAmount(groupId);
	}
	
	@Override
	public void payAmount(String userId, Integer groupId, PayAmountRequestDto payAmountRequest) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		payAmountRequest.setUserId(userId);
		payAmountRequest.setGroupId(groupId);
		financialRepository.payAmount(payAmountRequest);
	}
	
	@Override
	public void updateFee(String userId, Integer groupId, List<UpdateFeeRequestDto> updateFeeRequestList) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.FINANCE.getId())) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 회비를 설정할 수 있습니다.");
		}
		
		// 비즈니스 로직
		for(UpdateFeeRequestDto request : updateFeeRequestList) {
			request.setGroupId(groupId);
			financialRepository.updateFee(request);
		}
	}
	
	@Override
	public List<MemberExpenseInfoResponseDto> getMemberExpenseInfoList(String userId, Integer groupId) {
	
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.FINANCE.getId())) {
			throw new UnauthorizedRoleAccessException("'재무' 담당자만 회비 목록을 요청할 수 있습니다.");
		}
		
		// 비즈니스 로직
		return financialRepository.getMemberExpenseInfo(groupId, null);
	}
	
	@Override
	public MemberExpenseInfoResponseDto getMemberExpenseInfo(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		return financialRepository.getMemberExpenseInfo(groupId, userId).get(0);
	}
}
