package com.ssafy.pjt.financial.repository;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;

public interface FinancialRepository {
	void setFee(SetFeeRequestDto setFeeRequest);
	void payAmount(PayAmountRequestDto payAmountRequest);
	Integer getTotalAmount(Integer groupId);
}
