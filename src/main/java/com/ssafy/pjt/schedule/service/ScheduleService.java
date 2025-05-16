package com.ssafy.pjt.schedule.service;

import java.util.List;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;

public interface ScheduleService {
	void addScheduleProposal(String userId, Integer groupId, Integer attractionNo);
	List<GetProposalResponseDto> getProposalList(String userId, Integer groupId);
	
	void addContent(String userId, Integer groupId, AddContentRequestDto addContentRequest);
}
