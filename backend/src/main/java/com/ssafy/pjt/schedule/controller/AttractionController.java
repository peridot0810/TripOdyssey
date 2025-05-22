package com.ssafy.pjt.schedule.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;
import com.ssafy.pjt.schedule.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attraction")
public class AttractionController {

	private final AttractionService attractionService;

	@Operation(summary = "단일 장소 조회", description = "attractionNo를 통해 장소 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "장소 조회 성공")
	@GetMapping
	public ResponseEntity<?> getAttraction(
			@Parameter(description = "장소 ID", required = true, example = "56644") 
			@RequestParam(required = true) Integer attractionNo) {
		
		GetAttractionResponseDto attractionInfo = attractionService.getAttraction(attractionNo);
		return ResponseEntity.ok(new CommonResponse<>(true, "장소 조회에 성공했습니다.", attractionInfo));
	}

	@Operation(summary = "장소 목록 조회", description = "지역 코드, 콘텐츠 타입, 키워드 등 필터 조건을 기반으로 장소 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "장소 목록 조회 성공")
	@GetMapping("/list")
	public ResponseEntity<?> getAttractionList(
			@Parameter(description = "시도 코드", example = "1") 
			@RequestParam(required = false) Integer sidoCode,
			
			@Parameter(description = "구군 코드", example = "2") 
			@RequestParam(required = false) Integer gugunCode,
			
			@Parameter(description = "콘텐츠 타입 ID", example = "12") 
			@RequestParam(required = false) Integer contentTypeId,
			
			@Parameter(description = "검색 키워드", example = "한옥") 
			@RequestParam(required = false, defaultValue = "") String keyword,
			
			@Parameter(description = "페이지 번호", example = "1") 
			@RequestParam(required = false, defaultValue = "1") Integer page,
			
			@Parameter(description = "페이지당 항목 수", example = "5") 
			@RequestParam(required = false, defaultValue = "5") Integer perPage) {
		
		List<GetAttractionResponseDto> attractionList = attractionService.getAttractionList(sidoCode, gugunCode, contentTypeId, keyword, page, perPage);
		return ResponseEntity.ok(new CommonResponse<>(true, "장소 조회에 성공했습니다.", attractionList));
	}
}
