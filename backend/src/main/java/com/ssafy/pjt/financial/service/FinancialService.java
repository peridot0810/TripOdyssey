package com.ssafy.pjt.financial.service;

import java.util.List;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.MemberExpenseInfoResponseDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;

public interface FinancialService {

	void setFee(String userId, Integer groupId, SetFeeRequestDto setFeeRequest);
	void payAmount(String userId, Integer groupId, PayAmountRequestDto payAmountRequest);
	void updateFee(String userId, Integer groupId, List<UpdateFeeRequestDto> updateFeeRequestList);
	TotalAmountResponseDto getTotalAmount(String userId, Integer groupId);
	List<MemberExpenseInfoResponseDto> getMemberExpenseInfoList(String userId, Integer groupId);
	MemberExpenseInfoResponseDto getMemberExpenseInfo(String userId, Integer groupId);
}
