package com.ssafy.pjt.meet.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.meet.dto.request.AvailableSchedulePostRequest;
import com.ssafy.pjt.meet.dto.request.AvailableScheduleUpdateRequest;
import com.ssafy.pjt.meet.entity.AvailableSchedule;

@Mapper
public interface AvailableScheduleRepository {
	public int insertAvailableSchedule(AvailableSchedule availableSchedule);
	
	public int updateAvailableSchedule(AvailableScheduleUpdateRequest availableSchedule);
	
	public int deleteAvailableScheduleById(Integer id);
}
