package com.ssafy.pjt.financial.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.request.PayAmountRequestDto;
import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.financial.dto.request.SetFeeRequestDto;
import com.ssafy.pjt.financial.dto.request.UpdateFeeRequestDto;
import com.ssafy.pjt.financial.dto.response.TotalAmountResponseDto;
import com.ssafy.pjt.financial.service.FinancialService;
import com.ssafy.pjt.user.service.CustomUserDetailService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial/{groupId}")
public class FinancialController {

	private final JwtUtil jwtUtil;
	private final FinancialService financialService;
	
	@PostMapping("/budget")
	public ResponseEntity<?> setFee(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestBody SetFeeRequestDto setFeeRequest){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		
		financialService.setFee(userId, groupId, setFeeRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 설정에 성공했습니다.", null));
	}
	
	@PutMapping("/budget")
	public ResponseEntity<?> updateFee(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestBody List<UpdateFeeRequestDto> updateFeeRequestList){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		
		financialService.updateFee(userId, groupId, updateFeeRequestList);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 업데이트에 성공했습니다.", null));
	}
	
	@PutMapping("/budget/pay")
	public ResponseEntity<?> payAmount(@RequestHeader("Authorization") String token, @PathVariable Integer groupId, @RequestBody PayAmountRequestDto payAmountRequest){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		
		financialService.payAmount(userId, groupId, payAmountRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 지불에 성공했습니다.", null));
	}
	
	@GetMapping("/budget")
	public ResponseEntity<?> getTotalAmount(@RequestHeader("Authorization") String token, @PathVariable Integer groupId){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		
		TotalAmountResponseDto totalAmount = financialService.getTotalAmount(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 총합 조회에 성공했습니다.", totalAmount));
	}
}
