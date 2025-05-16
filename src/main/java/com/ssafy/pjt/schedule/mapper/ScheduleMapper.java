package com.ssafy.pjt.schedule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;

@Mapper
public interface ScheduleMapper {
	
	void addScheduleProposal(AddProposalRequestDto addProposalRequest);
	List<GetProposalResponseDto> getProposalList(Integer groupId);
}
