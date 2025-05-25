package com.ssafy.pjt.financial.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.ai.service.ExpenseTrackerAiChatService;
import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.financial.dto.request.AddExpenseRequestDto;
import com.ssafy.pjt.financial.dto.response.ExpenseInfoResponseDto;
import com.ssafy.pjt.financial.service.ExpenseTrackerService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial/expense-tracker/{groupId}")
public class ExpenseTrackerController {

	private final ExpenseTrackerService expenseTrackerService;
	private final ExpenseTrackerAiChatService expenseTrackerAiChatService;

	@Operation(summary = "가계부 조회", description = "그룹의 모든 가계부 항목을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "가계부 조회 성공")
	@GetMapping
	public ResponseEntity<?> getGroupExpenseTracker(
			@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId) {
		String userId = userDetails.getUsername();
		List<ExpenseInfoResponseDto> expenseTracker = expenseTrackerService.getExpenseTracker(userId, groupId);
		return ResponseEntity.ok(new CommonResponse<>(true, "가계부 조회에 성공했습니다.", expenseTracker));
	}

	@Operation(summary = "가계부 항목 추가", description = "새로운 가계부 항목을 추가합니다.")
	@ApiResponse(responseCode = "200", description = "가계부 항목 추가 성공")
	@PostMapping
	public ResponseEntity<?> addExpense(
			@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
			@RequestBody AddExpenseRequestDto expense) {
		String userId = userDetails.getUsername();
		Integer newExpenseId = expenseTrackerService.addExpense(userId, groupId, expense);
		return ResponseEntity.ok(new CommonResponse<>(true, "가계부 항목 추가에 성공했습니다.", Map.of("expenseId", newExpenseId)));
	}

	@Operation(summary = "가계부 항목 삭제", description = "지정한 가계부 항목을 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "가계부 항목 삭제 성공")
	@DeleteMapping
	public ResponseEntity<?> deleteExpense(
			@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
			@Parameter(description = "삭제할 가계부 항목 ID", example = "5") @RequestParam Integer expenseId) {
		String userId = userDetails.getUsername();
		expenseTrackerService.deleteExpense(userId, groupId, expenseId);
		return ResponseEntity.ok(new CommonResponse<>(true, "가계부 항목 삭제에 성공했습니다.", null));
	}
	
	@Operation(summary = "AI 지출 분석 요청", description = "지출 내역을 기반으로 AI가 요약 분석 결과를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "지출 분석 성공")
	@GetMapping("/summary")
	public ResponseEntity<?> generateExpenseTrackerSummary(
			@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
			@Parameter(description = "그룹 ID", example = "101") @PathVariable Integer groupId,
			@Parameter(description = "사용자 프롬프트", example = "지출 내역 분석해줘") @RequestParam(required=false) String userInput){
		
		String userId = userDetails.getUsername();
		List<ExpenseInfoResponseDto> expenseTracker = expenseTrackerService.getExpenseTracker(userId, groupId);
		String summary = expenseTrackerAiChatService.generateExpenseSummary(userInput, expenseTracker);
		return ResponseEntity.ok(new CommonResponse<>(true, "지출 내역 분석에 성공했습니다.", summary));
	}
	
}
