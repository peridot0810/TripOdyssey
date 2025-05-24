package com.ssafy.pjt.transportation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.mapper.CommonMapper;
import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;
import com.ssafy.pjt.transportation.mapper.TransportationMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransportationRepositoryImpl implements TransportationRepository{

	private final TransportationMapper transportationMapper;
	private final CommonMapper commonMapper;
	
	@Override
	public void addTransportation(AddTransportationRequestDto addRequest) {
		transportationMapper.addTransportation(addRequest);
	}
	
	@Override
	public void deleteTransportation(Integer transportationId) {
		transportationMapper.deleteTransportation(transportationId);
	}
	
	@Override
	public TransportationResponseDto getTransportation(Integer transportationId) {
		return transportationMapper.getTransportation(transportationId);
	}
	
	@Override
	public List<TransportationResponseDto> getTransportationList(Integer groupId) {
		return transportationMapper.getTransportationList(groupId);
	}
	
	@Override
	public void updateTransportation(UpdateTransportationRequestDto updateRequest) {
		transportationMapper.updateTransportation(updateRequest);
	}
	
	@Override
	public Boolean checkUserInGroup(UserGroupRequestDto checkUserInGroupRequest) {
		return commonMapper.checkUserInGroup(checkUserInGroupRequest);
	}
	
	@Override
	public List<Integer> getUserRole(UserGroupRequestDto userRoleRequest) {
		return commonMapper.getUserRoleInGroup(userRoleRequest);
	}
}
