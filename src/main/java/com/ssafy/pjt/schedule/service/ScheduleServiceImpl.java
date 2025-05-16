package com.ssafy.pjt.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
	private final ScheduleRepository scheduleRepository;
	private final UserValidationService userValidationService;

	@Override
	public void addScheduleProposal(String userId, Integer groupId, Integer attractionNo) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		
		scheduleRepository.addScheduleProposal(AddProposalRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.attractionsNo(attractionNo)
				.build());
	}
	
	@Override
	public List<GetProposalResponseDto> getProposalList(String userId, Integer groupId) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		List<GetProposalResponseDto> proposalList = scheduleRepository.getProposalList(groupId);
		return proposalList;
	}
	
	@Override
	public void addContent(String userId, Integer groupId, AddContentRequestDto addContentRequest) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 유저의 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("교통/숙소 담당자만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		// content 테이블에 추가
		scheduleRepository.addContent(addContentRequest);
		
		// schedule 테이블에 추가
		scheduleRepository.addSchedule(AddScheduleRequestDto.builder()
				.contentId(addContentRequest.getContentId())
				.groupId(groupId)
				.build());
	}
	
}
