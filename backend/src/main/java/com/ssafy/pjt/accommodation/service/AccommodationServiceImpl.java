package com.ssafy.pjt.accommodation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;
import com.ssafy.pjt.accommodation.repository.AccommodationRepository;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.schedule.service.AttractionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService{
	private final AccommodationRepository accommodationRepository;
	private final UserValidationService userValidationService;
	private final AttractionService attractionService;
	
	@Override
	public Integer addAccommodation(String userId, Integer groupId, AddAccommodationRequestDto addAccommodationRequest) {
		
		// 유저 역할 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("교통/숙소 담당만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		addAccommodationRequest.setGroupId(groupId);
		accommodationRepository.addAccommodation(addAccommodationRequest);
		return addAccommodationRequest.getAccommodationId();
	}
	
	@Override
	public void updateAccommodation(String userId, Integer groupId, Integer accommodationId,
			UpdateAccommodationRequestDto updateAccommodationRequest) {
		
		// 유저 역할 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("교통/숙소 담당만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		updateAccommodationRequest.setAccommodationId(accommodationId);
		accommodationRepository.updateAccommodation(updateAccommodationRequest);
	}
	
	@Override
	public void deleteAccommodation(String userId, Integer groupId, Integer accommodationId) {
		// 유저 역할 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("교통/숙소 담당만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		accommodationRepository.deleteAccommodation(accommodationId);
	}
	
	@Override
	public GetAccommodationResponseDto getAccommodation(String userId, Integer groupId, Integer accommodationId) {

		// 유저 역할 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		GetAccommodationResponseDto accommodation = accommodationRepository.getAccommodation(accommodationId);
		
		if(accommodation.getAttractionsNo() != null) {
			accommodation.setAccommodationInfo(attractionService.getAttraction(accommodation.getAttractionsNo()));
		}
		
		return accommodation;
	}
	
	@Override
	public List<GetAccommodationResponseDto> getAccommodationList(String userId, Integer groupId) {
	
		// 유저 역할 확인
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("그룹원만 요청할 수 있는 기능입니다.");
		}
		
		// 비즈니스 로직
		List<GetAccommodationResponseDto> accommodationList = accommodationRepository.getAccommodationList(groupId); 
		
		for(GetAccommodationResponseDto accommodation : accommodationList) {
			if(accommodation.getAttractionsNo() != null) {
				accommodation.setAccommodationInfo(attractionService.getAttraction(accommodation.getAttractionsNo()));
			}
		}
		
		return accommodationList;
	}
	
	
}
