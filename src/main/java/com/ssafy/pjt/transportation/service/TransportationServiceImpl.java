package com.ssafy.pjt.transportation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.financial.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.financial.exception.UserNotInGroupException;
import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;
import com.ssafy.pjt.transportation.repository.TransportationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService{
	
	private final TransportationRepository transportationRepository;

	@Override
	public void addTransportation(String userId, Integer groupId, AddTransportationRequestDto addRequest) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		Integer userRole = transportationRepository.getUserRole(new UserGroupRequestDto(userId, groupId));
		if(userRole != 4) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 추가할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		addRequest.setGroupId(groupId);
		transportationRepository.addTransportation(addRequest);
	}
	
	@Override
	public void deleteTransportation(String userId, Integer groupId, Integer transportationId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		Integer userRole = transportationRepository.getUserRole(new UserGroupRequestDto(userId, groupId));
		if(userRole != 4) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 삭제할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		transportationRepository.deleteTransportation(transportationId);
	}
	
	
	
	@Override
	public void updateTransportation(String userId, Integer groupId, UpdateTransportationRequestDto updateRequest) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인 
		Integer userRole = transportationRepository.getUserRole(new UserGroupRequestDto(userId, groupId));
		if(userRole != 4) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 수정할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		transportationRepository.updateTransportation(updateRequest);
	}
	
	
	@Override
	public TransportationResponseDto getTransportation(String userId, Integer groupId, Integer transportationId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직 
		return transportationRepository.getTransportation(transportationId);
	}
	
	@Override
	public List<TransportationResponseDto> getTransportationList(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		return transportationRepository.getTransportationList(groupId);
	}
}
