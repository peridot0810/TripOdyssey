package com.ssafy.pjt.meet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.CommonResponse;
import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.dto.response.AvailableScheduleResponse;
import com.ssafy.pjt.meet.entity.AvailableSchedule;
import com.ssafy.pjt.meet.repository.AvailableScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAvailableScheduleService implements AvailableScheduleService {
	private final AvailableScheduleRepository asDao;

	@Override
	public CommonResponse<AvailableScheduleResponse> addAvailableSchedule(AvailableSchedulePostRequest schedulePostRequest) {
	    AvailableSchedule schedule = AvailableSchedule.builder()
	        .userId(schedulePostRequest.getUserId())
	        .groupId(schedulePostRequest.getGroupId())
	        .startDate(schedulePostRequest.getStartDate())
	        .endDate(schedulePostRequest.getEndDate())
	        .build();

	    asDao.insertAvailableSchedule(schedule);
	    
	    AvailableScheduleResponse responseData = AvailableScheduleResponse.builder()
	    		.id(schedule.getId())
	    		.startDate(schedule.getStartDate())
	    		.endDate(schedule.getEndDate())
	    		.build();

	    return new CommonResponse<AvailableScheduleResponse>(true, "일정 입력 성공", responseData);
	}

	@Override
	public CommonResponse<AvailableScheduleResponse> updateAvailableSchedule(AvailableScheduleUpdateRequest scheduleUpdateRequest) {

		    asDao.updateAvailableSchedule(scheduleUpdateRequest);
		    
		    AvailableScheduleResponse responseData = AvailableScheduleResponse.builder()
    			.id(scheduleUpdateRequest.getId())
    			.startDate(scheduleUpdateRequest.getStartDate())
    			.endDate(scheduleUpdateRequest.getEndDate())
    			.build();

		    return new CommonResponse<AvailableScheduleResponse>(true, "일정 수정 성공", responseData);
	}

	@Override
	public CommonResponse<Void> deleteAvailableScheduleById(Integer availableScheduleId) {
		asDao.deleteAvailableScheduleById(availableScheduleId);
		return new CommonResponse<Void>(true, "일정 삭제 성공", null);
	}

	@Override
	public CommonResponse<List<AvailableScheduleResponse>> findAvailableScheduleOfUser(String userId, int groupId) {
		List<AvailableSchedule> availableScheduleList = asDao.findAllByUserIdAndGroupId(userId, groupId);
		
		List<AvailableScheduleResponse> availableDateRanges = availableScheduleList.stream()
				.map(schedule -> AvailableScheduleResponse.builder()
					.id(schedule.getId())
					.startDate(schedule.getStartDate())
					.endDate(schedule.getEndDate())
					.build()
				).toList();
		
		return new CommonResponse<List<AvailableScheduleResponse>>(true, "유저 가능한 일정 조회 성공", availableDateRanges);
	}
	
	@Override
	public CommonResponse<List<AvailableSchedule>> findAvailableScheduleOfGroup(int groupId) {
		List<AvailableSchedule> availableScheduleList = asDao.findAllByGroupId(groupId);

		return new CommonResponse<List<AvailableSchedule>>(true, "그룹원들이 가능한 일정 조회 성공", availableScheduleList);
	}


}
