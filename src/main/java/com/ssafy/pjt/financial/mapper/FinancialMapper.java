package com.ssafy.pjt.financial.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;

@Mapper
public interface FinancialMapper {
	void setFee(SetFeeRequestDto setFeeRequest);
}
