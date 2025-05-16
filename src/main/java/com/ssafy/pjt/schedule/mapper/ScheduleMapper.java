package com.ssafy.pjt.schedule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;

@Mapper
public interface ScheduleMapper {
	
	void addScheduleProposal(AddProposalRequestDto addProposalRequest);
	List<GetProposalResponseDto> getProposalList(Integer groupId);
	
	void addContent(AddContentRequestDto addContentRequest);
	void addSchedule(AddScheduleRequestDto addScheduleRequest);
}
