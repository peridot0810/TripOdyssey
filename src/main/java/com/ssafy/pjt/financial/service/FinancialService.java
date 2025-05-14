package com.ssafy.pjt.financial.service;

import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;

public interface FinancialService {

	void setFee(String userId, Integer groupId, SetFeeRequestDto setFeeRequest);
	
}
