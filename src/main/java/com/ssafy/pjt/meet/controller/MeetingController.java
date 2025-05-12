package com.ssafy.pjt.meet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableSchedulePostResponse;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleUpdateResponse;
import com.ssafy.pjt.meet.dto.response.CommonResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;
import com.ssafy.pjt.meet.service.AvailableScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {
	
	private final AvailableScheduleService sService;
	
	@PostMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableSchedulePostResponse>> postSchedule(
	        @RequestBody AvailableSchedulePostRequest schedulePostRequest) {
	    
	    CommonResponse<AvailableSchedulePostResponse> commonResponse = sService.addAvailableSchedule(schedulePostRequest);
	    return ResponseEntity.ok(commonResponse);
	}
	
	@PutMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableScheduleUpdateResponse>> putSchedule(
	        @RequestBody AvailableScheduleUpdateRequest scheduleUpdateRequest) {
	    
	    CommonResponse<AvailableScheduleUpdateResponse> commonResponse = sService.updateAvailableSchedule(scheduleUpdateRequest);
	    return ResponseEntity.ok(commonResponse);
	}
	
	@DeleteMapping("/schedule/{id}")
	public ResponseEntity<?> deleteSchedule(@PathVariable("id") Integer availableScheduleId) {
		sService.deleteAvailableScheduleById(availableScheduleId);
	    return ResponseEntity.ok(new CommonResponse<>(true, "삭제 성공", null));
	}

	

}
