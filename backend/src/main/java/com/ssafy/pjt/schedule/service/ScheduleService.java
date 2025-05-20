package com.ssafy.pjt.schedule.service;

import java.util.List;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateScheduleRequestWrapperDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;

public interface ScheduleService {
	
	// Proposal
	Integer addScheduleProposal(String userId, Integer groupId, Integer attractionNo);
	List<GetProposalResponseDto> getProposalList(String userId, Integer groupId);
	void likeProposal(String userId, Integer groupId, Integer proposalId);
	
	// Content
	Integer addContent(String userId, Integer groupId, AddContentRequestDto addContentRequest);
	void updateContent(String userId, Integer groupId, UpdateContentRequestDto updateContentRequest);
	void deleteContent(String userId, Integer groupId, Integer contentId);
	
	// Schedule
	List<GetScheduleResponseDto> getScheduleList(String userId, Integer groupId);
	void updateSchedules(String userId, Integer groupId, UpdateScheduleRequestWrapperDto updateScheduleRequest);
}
