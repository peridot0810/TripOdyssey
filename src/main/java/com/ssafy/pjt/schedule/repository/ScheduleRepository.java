package com.ssafy.pjt.schedule.repository;

import java.util.List;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;

public interface ScheduleRepository {
	void addScheduleProposal(AddProposalRequestDto addProposalRequest);
	List<GetProposalResponseDto> getProposalList(Integer groupId);
	
	void addContent(AddContentRequestDto addContentRequest);
	void addSchedule(AddScheduleRequestDto addScheduleRequest);
}
