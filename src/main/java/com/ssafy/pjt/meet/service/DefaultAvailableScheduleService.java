package com.ssafy.pjt.meet.service;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableSchedulePostResponse;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleUpdateResponse;
import com.ssafy.pjt.meet.dto.response.CommonResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;
import com.ssafy.pjt.meet.repository.AvailableScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAvailableScheduleService implements AvailableScheduleService {
	private final AvailableScheduleRepository asDao;

	@Override
	public CommonResponse<AvailableSchedulePostResponse> addAvailableSchedule(AvailableSchedulePostRequest schedulePostRequest) {
	    AvailableSchedule schedule = AvailableSchedule.builder()
	        .userId(schedulePostRequest.getUserId())
	        .groupId(schedulePostRequest.getGroupId())
	        .startDate(schedulePostRequest.getStartDate())
	        .endDate(schedulePostRequest.getEndDate())
	        .build();

	    asDao.insertAvailableSchedule(schedule);
	    
	    AvailableSchedulePostResponse responseData = AvailableSchedulePostResponse.builder()
	    		.id(schedule.getId())
	    		.startDate(schedule.getStartDate())
	    		.endDate(schedule.getEndDate())
	    		.build();

	    return new CommonResponse<AvailableSchedulePostResponse>(true, "일정 입력 성공", responseData);
	}

	@Override
	public CommonResponse<AvailableScheduleUpdateResponse> updateAvailableSchedule(AvailableScheduleUpdateRequest scheduleUpdateRequest) {

		    asDao.updateAvailableSchedule(scheduleUpdateRequest);
		    
		    AvailableScheduleUpdateResponse responseData = AvailableScheduleUpdateResponse.builder()
    			.id(scheduleUpdateRequest.getId())
    			.startDate(scheduleUpdateRequest.getStartDate())
    			.endDate(scheduleUpdateRequest.getEndDate())
    			.build();

		    return new CommonResponse<AvailableScheduleUpdateResponse>(true, "일정 수정 성공", responseData);
	}

	@Override
	public CommonResponse<Void> deleteAvailableScheduleById(Integer availableScheduleId) {
		asDao.deleteAvailableScheduleById(availableScheduleId);
		return new CommonResponse<Void>(true, "일정 삭제 성공", null);
	}


}
