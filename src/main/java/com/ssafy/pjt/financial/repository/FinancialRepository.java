package com.ssafy.pjt.financial.repository;

import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;

public interface FinancialRepository {
	void setFee(SetFeeRequestDto setFeeRequest);
}
