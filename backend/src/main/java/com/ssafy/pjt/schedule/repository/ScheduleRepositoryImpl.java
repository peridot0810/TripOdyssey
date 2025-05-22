package com.ssafy.pjt.schedule.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.GetAttractionListRequestDto;
import com.ssafy.pjt.schedule.dto.request.GetProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.LikeProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.ModifiedOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.NewOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.RemovedOfficialScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;
import com.ssafy.pjt.schedule.mapper.ScheduleMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository{

	private final ScheduleMapper scheduleMapper;
	
	@Override
	public void addScheduleProposal(AddProposalRequestDto addProposalRequest) {
		scheduleMapper.addScheduleProposal(addProposalRequest);
	}
	
	@Override
	public List<GetProposalResponseDto> getProposalList(GetProposalRequestDto getProposalRequest) {
		return scheduleMapper.getProposalList(getProposalRequest);
	}
	
	@Override
	public void likeProposal(LikeProposalRequestDto likeProposalRequest) {
		scheduleMapper.likeProposal(likeProposalRequest);
	}
	
	@Override
	public void unlikeProposal(LikeProposalRequestDto likeProposalRequest) {
		scheduleMapper.unlikeProposal(likeProposalRequest);
	}
	
	@Override
	public void addContent(AddContentRequestDto addContentRequest) {
		scheduleMapper.addContent(addContentRequest);
	}
	
	@Override
	public void addSchedule(AddScheduleRequestDto addScheduleRequest) {
		scheduleMapper.addSchedule(addScheduleRequest);
	}
	
	@Override
	public void updateContent(UpdateContentRequestDto updateContentRequest) {
		scheduleMapper.updateContent(updateContentRequest);
	}
	
	@Override
	public void deleteContent(Integer contentId) {
		scheduleMapper.deleteContent(contentId);	
	}
	
	@Override
	public List<GetScheduleResponseDto> getScheduleList(Integer groupId) {
		return scheduleMapper.getScheduleList(groupId);
	}
	
	@Override
	public GetAttractionResponseDto getAttractionInfo(Integer attractionNo) {
		return scheduleMapper.getAttractionInfo(attractionNo);
	}
	
	@Override
	public List<GetAttractionResponseDto> getAttractionList(GetAttractionListRequestDto searchCondition) {
		return scheduleMapper.getAttractionList(searchCondition);
	}
	
	@Override
	public void updateScheduleModified(List<ModifiedOfficialScheduleRequestDto> modifiedScheduleList) {
		scheduleMapper.updateScheduleModified(modifiedScheduleList);
	}
	@Override
	public void updateScheduleNew(List<NewOfficialScheduleRequestDto> newScheduleLiust) {
		scheduleMapper.updateScheduleNew(newScheduleLiust);
	}
	@Override
	public void UpdateScheduleRemoved(List<RemovedOfficialScheduleRequestDto> removedScheduleList) {
		scheduleMapper.updateScheduleRemoved(removedScheduleList);
	}
}
