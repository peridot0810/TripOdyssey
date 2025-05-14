package com.ssafy.pjt.meet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;
import com.ssafy.pjt.meet.service.AvailableScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {
	
	private final AvailableScheduleService sService;
	
	//@GetMapping("/schedule")
	
	
	@PostMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableScheduleResponse>> postSchedule(
	        @RequestBody AvailableSchedulePostRequest schedulePostRequest) {
	    
	    CommonResponse<AvailableScheduleResponse> commonResponse = sService.addAvailableSchedule(schedulePostRequest);
	    return ResponseEntity.ok(commonResponse);
	}
	
	@PutMapping("/schedule")
	public ResponseEntity<CommonResponse<AvailableScheduleResponse>> putSchedule(
	        @RequestBody AvailableScheduleUpdateRequest scheduleUpdateRequest) {
	    
	    CommonResponse<AvailableScheduleResponse> commonResponse = sService.updateAvailableSchedule(scheduleUpdateRequest);
	    return ResponseEntity.ok(commonResponse);
	}
	
	@DeleteMapping("/schedule/{id}")
	public ResponseEntity<?> deleteSchedule(@PathVariable("id") Integer availableScheduleId) {
		sService.deleteAvailableScheduleById(availableScheduleId);
	    return ResponseEntity.ok(new CommonResponse<>(true, "삭제 성공", null));
	}
	
	@GetMapping("/schedule/{groupId}")
	public ResponseEntity<?> getAllUserSchedule(@PathVariable Integer groupId, @RequestParam String userId) {
		CommonResponse<List<AvailableScheduleResponse>> getResponse = sService.findAvailableScheduleOfUser(userId, groupId);
	    return ResponseEntity.ok(getResponse);
	}
	
	@GetMapping("/schedule/all/{groupId}")
	public ResponseEntity<?> getAllSchedule(@PathVariable Integer groupId) {
		CommonResponse<List<AvailableSchedule>> getResponse = sService.findAvailableScheduleOfGroup(groupId);
	    return ResponseEntity.ok(getResponse);
	}

}
