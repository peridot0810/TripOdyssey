package com.ssafy.pjt.financial.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.CommonResponse;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestWrapperDto;
import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;
import com.ssafy.pjt.financial.service.ExpenseTrackerService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial/expense-tracker")
public class ExpenseTrackerController {

	private final ExpenseTrackerService expenseTrackerService;
	private final JwtUtil jwtUtil;
	
	@GetMapping
	public ResponseEntity<?> getGroupExpenseTracker(@RequestHeader("Authorization") String token, @RequestParam Integer groupId){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		
		List<ExpenseInfoResponseDto> expenseTracker = expenseTrackerService.getExpenseTracker(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "가계부 조회에 성공했습니다.", expenseTracker));
	}
	
	@PostMapping
	public ResponseEntity<?> addExpense(@RequestHeader("Authorization") String token, @RequestBody AddExpenseRequestWrapperDto request ){
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.extractUserId(token);
		Integer groupId = request.getGroupId();
		AddExpenseRequestDto expense = request.getNewExpense();
		
		expenseTrackerService.addExpense(userId, groupId, expense);
		return ResponseEntity.ok(new CommonResponse<>(true, "가계부 항목 추가에 성공했습니다.", null));
	}
}
