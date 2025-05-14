package com.ssafy.pjt.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.service.TravelGroupService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class TravelGroupController {
	
	private final TravelGroupService travelGroupService;
	
	@PostMapping
	public ResponseEntity<CommonResponse<TravelGroupPostResponse>> createTravelGroup(@RequestBody TravelGroupPostRequest travelGroupPostRequest){
		CommonResponse<TravelGroupPostResponse> responseData = travelGroupService.createTravelGroup(travelGroupPostRequest);
		
		return ResponseEntity.ok(responseData);
	}
}
