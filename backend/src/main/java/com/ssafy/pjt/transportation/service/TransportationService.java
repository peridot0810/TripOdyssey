package com.ssafy.pjt.transportation.service;

import java.util.List;

import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;


public interface TransportationService {

	List<TransportationResponseDto> getTransportationList(String userId, Integer groupId);
	TransportationResponseDto getTransportation(String userId, Integer groupId, Integer transportationId);
	Integer addTransportation(String userId, Integer groupId, AddTransportationRequestDto addRequest);
	void updateTransportation(String userId, Integer groupId, UpdateTransportationRequestDto updateRequest);
	void deleteTransportation(String userId, Integer groupId, Integer transportationId);
}
