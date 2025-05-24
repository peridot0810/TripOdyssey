package com.ssafy.pjt.financial.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.MemberExpenseInfoResponseDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;
import com.ssafy.pjt.financial.mapper.FinancialMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FinancialRepositoryImpl implements FinancialRepository{
	
	private final FinancialMapper financialMapper;
	
	@Override
	public void setFee(SetFeeRequestDto setFeeRequest) {
		financialMapper.setFee(setFeeRequest);
	}
	
	@Override
	public void payAmount(PayAmountRequestDto payAmountRequest) {
		financialMapper.payAmount(payAmountRequest);
	}
	
	@Override
	public void updateFee(UpdateFeeRequestDto updateFeeRequest) {
		financialMapper.updateFee(updateFeeRequest);
	}
	
	@Override
	public TotalAmountResponseDto getTotalAmount(Integer groupId) {
		return financialMapper.getTotalAmount(groupId);
	}
	
	@Override
	public List<MemberExpenseInfoResponseDto> getMemberExpenseInfo(Integer groupId, String userId) {
		return financialMapper.getMemberExpenseInfo(groupId, userId);
	}
}
