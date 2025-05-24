package com.ssafy.pjt.financial.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.MemberExpenseInfoResponseDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;

@Mapper
public interface FinancialMapper {
	void setFee(SetFeeRequestDto setFeeRequest);
	void payAmount(PayAmountRequestDto payAmountRequest);
	void updateFee(UpdateFeeRequestDto updateFeeRequest);
	TotalAmountResponseDto getTotalAmount(Integer groupId);
	List<MemberExpenseInfoResponseDto> getMemberExpenseInfo(Integer groupId, String userId);
}
