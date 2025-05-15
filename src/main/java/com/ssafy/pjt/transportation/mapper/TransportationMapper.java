package com.ssafy.pjt.transportation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;

@Mapper
public interface TransportationMapper {

	List<TransportationResponseDto> getTransportationList(Integer groupId);
	TransportationResponseDto getTransportation(Integer transportationId);
	void addTransportation(AddTransportationRequestDto addRequest);
	void updateTransportation(UpdateTransportationRequestDto updateRequest);
	void deleteTransportation(Integer transportationId);
}
