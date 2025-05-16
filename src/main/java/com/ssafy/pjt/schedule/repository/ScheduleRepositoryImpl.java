package com.ssafy.pjt.schedule.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
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
	public List<GetProposalResponseDto> getProposalList(Integer groupId) {
		return scheduleMapper.getProposalList(groupId);
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
}
