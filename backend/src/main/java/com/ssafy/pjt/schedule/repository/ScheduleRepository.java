package com.ssafy.pjt.schedule.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.GetProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.LikeProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.ModifiedOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.NewOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.RemovedOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;

public interface ScheduleRepository {
	
	// Proposal
	void addScheduleProposal(AddProposalRequestDto addProposalRequest);
	List<GetProposalResponseDto> getProposalList(GetProposalRequestDto getProposalRequest);
	void likeProposal(LikeProposalRequestDto likeProposalRequest);
	void unlikeProposal(LikeProposalRequestDto likeProposalRequest);
	
	// Content
	void addContent(AddContentRequestDto addContentRequest);
	void updateContent(UpdateContentRequestDto updateContentRequest);
	void deleteContent(Integer contentId);
	
	// Schedule
	void addSchedule(AddScheduleRequestDto addScheduleRequest);
	List<GetScheduleResponseDto> getScheduleList(Integer groupId);
	void updateScheduleNew(List<NewOfficialScheduleRequestDto> newScheduleLiust);
	void updateScheduleModified(List<ModifiedOfficialScheduleRequestDto> modifiedScheduleList);
	void UpdateScheduleRemoved(List<RemovedOfficialScheduleRequestDto> removedScheduleList);
	
	// Attraction
	GetAttractionResponseDto getAttractionInfo(Integer attractionNo);
}
