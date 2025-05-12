package com.ssafy.pjt.meet.service;

import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableSchedulePostResponse;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleUpdateResponse;
import com.ssafy.pjt.meet.dto.response.CommonResponse;

public interface AvailableScheduleService {
	public CommonResponse<AvailableSchedulePostResponse> addAvailableSchedule(AvailableSchedulePostRequest schedulePostRequest);
	
	public CommonResponse<AvailableScheduleUpdateResponse> updateAvailableSchedule(AvailableScheduleUpdateRequest scheduleUpdateRequest);
	
	public CommonResponse<Void> deleteAvailableScheduleById(Integer availableScheduleId);
}
