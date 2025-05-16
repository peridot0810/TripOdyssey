package com.ssafy.pjt.schedule.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
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
}
