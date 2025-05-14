package com.ssafy.pjt.meet.service;

import java.util.List;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;

public interface AvailableScheduleService {
	public CommonResponse<AvailableScheduleResponse> addAvailableSchedule(AvailableSchedulePostRequest schedulePostRequest);
	
	public CommonResponse<AvailableScheduleResponse> updateAvailableSchedule(AvailableScheduleUpdateRequest scheduleUpdateRequest);
	
	public CommonResponse<Void> deleteAvailableScheduleById(Integer availableScheduleId);
	
	public CommonResponse<List<AvailableScheduleResponse>> findAvailableScheduleOfUser(String userId, int groupId);
	
	public CommonResponse<List<AvailableSchedule>> findAvailableScheduleOfGroup(int groupId);
}
