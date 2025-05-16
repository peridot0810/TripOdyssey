package com.ssafy.pjt.schedule.repository;

import java.util.List;

import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;

public interface ScheduleRepository {
	void addScheduleProposal(AddProposalRequestDto addProposalRequest);
	List<GetProposalResponseDto> getProposalList(Integer groupId);
}
