package com.ssafy.pjt.financial.repository;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;

public interface FinancialRepository {
	void setFee(SetFeeRequestDto setFeeRequest);
	void payAmount(PayAmountRequestDto payAmountRequest);
	void updateFee(UpdateFeeRequestDto updateFeeRequest);
	TotalAmountResponseDto getTotalAmount(Integer groupId);
}
