package com.ssafy.pjt.financial.service;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;

public interface FinancialService {

	void setFee(String userId, Integer groupId, SetFeeRequestDto setFeeRequest);
	Integer getTotalAmount(String userId, Integer groupId);
	void payAmount(String userId, Integer groupId, PayAmountRequestDto payAmountRequest);
}
