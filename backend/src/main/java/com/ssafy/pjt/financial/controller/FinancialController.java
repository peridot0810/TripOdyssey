package com.ssafy.pjt.financial.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial/{groupId}")
public class FinancialController {

	private final JwtUtil jwtUtil;
	private final FinancialService financialService;

	@Operation(summary = "회비 설정", description = "그룹의 회비를 설정합니다.")
	@ApiResponse(responseCode = "200", description = "회비 설정 성공")
	@PostMapping("/budget")
	public ResponseEntity<?> setFee(
			@AuthenticationPrincipal UserDetails userDetails,
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
	        @RequestBody SetFeeRequestDto setFeeRequest) {

		String userId = userDetails.getUsername();
		financialService.setFee(userId, groupId, setFeeRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 설정에 성공했습니다.", null));
	}

	@Operation(summary = "회비 수정", description = "그룹의 회비 정보를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "회비 수정 성공")
	@PutMapping("/budget")
	public ResponseEntity<?> updateFee(
			@AuthenticationPrincipal UserDetails userDetails,
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
	        @RequestBody List<UpdateFeeRequestDto> updateFeeRequestList) {

		String userId = userDetails.getUsername();
		financialService.updateFee(userId, groupId, updateFeeRequestList);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 업데이트에 성공했습니다.", null));
	}

	@Operation(summary = "회비 지불", description = "지정된 회비를 지불합니다.")
	@ApiResponse(responseCode = "200", description = "회비 지불 성공")
	@PutMapping("/budget/pay")
	public ResponseEntity<?> payAmount(
			@AuthenticationPrincipal UserDetails userDetails,
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
	        @RequestBody PayAmountRequestDto payAmountRequest) {

		String userId = userDetails.getUsername();
		financialService.payAmount(userId, groupId, payAmountRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 지불에 성공했습니다.", null));
	}

	@Operation(summary = "회비 총합 조회", description = "현재까지 지불된 회비의 총합을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "회비 총합 조회 성공")
	@GetMapping("/budget")
	public ResponseEntity<?> getTotalAmount(
			@AuthenticationPrincipal UserDetails userDetails,
	        @Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId) {

		String userId = userDetails.getUsername();
		TotalAmountResponseDto totalAmount = financialService.getTotalAmount(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "회비 총합 조회에 성공했습니다.", totalAmount));
	}
}
