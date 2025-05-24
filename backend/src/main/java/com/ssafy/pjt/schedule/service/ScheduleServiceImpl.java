package com.ssafy.pjt.schedule.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.schedule.dto.request.AddContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.AddScheduleRequestDto;
import com.ssafy.pjt.schedule.dto.request.GetProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.LikeProposalRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateContentRequestDto;
import com.ssafy.pjt.schedule.dto.request.UpdateScheduleRequestWrapperDto;
import com.ssafy.pjt.schedule.dto.response.GetProposalResponseDto;
import com.ssafy.pjt.schedule.dto.response.GetScheduleResponseDto;
import com.ssafy.pjt.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
	private final ScheduleRepository scheduleRepository;
	private final UserValidationService userValidationService;

	@Override
	public Integer addScheduleProposal(String userId, Integer groupId, Integer attractionNo) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		AddProposalRequestDto newProposal = AddProposalRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.attractionsNo(attractionNo)
				.build();
		
		scheduleRepository.addScheduleProposal(newProposal);
		
		return newProposal.getProposalId();
	}
	
	@Override
	public List<GetProposalResponseDto> getProposalList(String userId, Integer groupId) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		List<GetProposalResponseDto> proposalList = scheduleRepository.getProposalList(GetProposalRequestDto.builder()
				.userId(userId)
				.groupId(groupId)
				.build());
		return proposalList;
	}
	
	@Override
	public Boolean likeProposal(String userId, Integer groupId, Integer proposalId) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		LikeProposalRequestDto likeProposalRequest = LikeProposalRequestDto.builder()
				.userId(userId)
				.proposalId(proposalId)
				.build();
		
		try {
			// 좋아요 누르기 
			scheduleRepository.likeProposal(likeProposalRequest);
			return true;
		} catch (DuplicateKeyException e) {
			// 좋아요 취소하기
			scheduleRepository.unlikeProposal(likeProposalRequest);
			return false;
		}
		
	}
	
	@Override
	public Integer addContent(String userId, Integer groupId, AddContentRequestDto addContentRequest) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 유저의 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.SCHEDULE.getId())) {
			throw new UnauthorizedRoleAccessException("일정 담당자만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		// content 테이블에 추가
		scheduleRepository.addContent(addContentRequest);
		
		// schedule 테이블에 추가
		scheduleRepository.addSchedule(AddScheduleRequestDto.builder()
				.contentId(addContentRequest.getContentId())
				.groupId(groupId)
				.build());
		
		return addContentRequest.getContentId();
	}
	
	@Override
	public void updateContent(String userId, Integer groupId, UpdateContentRequestDto updateContentRequest) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 유저의 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.SCHEDULE.getId())) {
			throw new UnauthorizedRoleAccessException("일정 담당자만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		scheduleRepository.updateContent(updateContentRequest);
	}
	
	@Override
	public void deleteContent(String userId, Integer groupId, Integer contentId) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 유저의 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.SCHEDULE.getId())) {
			throw new UnauthorizedRoleAccessException("일정 담당자만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		scheduleRepository.deleteContent(contentId);
	}
	
	@Override
	public List<GetScheduleResponseDto> getScheduleList(String userId, Integer groupId) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직 
		List<GetScheduleResponseDto> scheduleList = scheduleRepository.getScheduleList(groupId);
		for(GetScheduleResponseDto schedule :scheduleList) {
			if(schedule.getAttractionsNo() != null) {
				schedule.setAttractionInfo(scheduleRepository.getAttractionInfo(schedule.getAttractionsNo()));
			}
		}
		
		return scheduleList;
	}
	
	@Override
	public void updateSchedules(String userId, Integer groupId, UpdateScheduleRequestWrapperDto updateScheduleRequest) {
		// 유저가 그룹원이 맞는지 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 유저의 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.SCHEDULE.getId())) {
			throw new UnauthorizedRoleAccessException("일정 담당자만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		log.debug("removedOfficialSchedules :{}",updateScheduleRequest.getRemovedOfficialSchedules());
		if(updateScheduleRequest.getRemovedOfficialSchedules() != null) {
			scheduleRepository.UpdateScheduleRemoved(updateScheduleRequest.getRemovedOfficialSchedules());
		}
		
		log.debug("modifiedOfficialSchedules :{}",updateScheduleRequest.getModifiedOfficialSchedules());
		if(updateScheduleRequest.getModifiedOfficialSchedules() != null) {
			scheduleRepository.updateScheduleModified(updateScheduleRequest.getModifiedOfficialSchedules());
		}
		
		
		log.debug("newOfficialSchedules :{}",updateScheduleRequest.getNewOfficialSchedules());
		if(updateScheduleRequest.getNewOfficialSchedules() != null) {
			scheduleRepository.updateScheduleNew(updateScheduleRequest.getNewOfficialSchedules());
		}
	}
	
}
