package com.ssafy.pjt.transportation.repository;

import java.util.List;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;

public interface TransportationRepository {
	List<TransportationResponseDto> getTransportationList(Integer groupId);
	TransportationResponseDto getTransportation(Integer transportationId);
	void addTransportation(AddTransportationRequestDto addRequest);
	void updateTransportation(UpdateTransportationRequestDto updateRequest);
	void deleteTransportation(Integer transportationId);
	
	// 공통 기능 
	Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest);
	List<Integer> getUserRole(UserGroupRequestDto userRoleRequest);
}
