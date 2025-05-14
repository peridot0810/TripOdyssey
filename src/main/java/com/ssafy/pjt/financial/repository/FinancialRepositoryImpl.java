package com.ssafy.pjt.financial.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
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
}
