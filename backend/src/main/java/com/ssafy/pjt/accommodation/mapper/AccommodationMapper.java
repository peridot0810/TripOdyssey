package com.ssafy.pjt.accommodation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;

@Mapper
public interface AccommodationMapper {
	void addAccommodation(AddAccommodationRequestDto addAccommodationRequest);
	void updateAccommodation(UpdateAccommodationRequestDto updateAccommodationRequest);
	void deleteAccommodation(Integer accommodationId);
	
	// get
	GetAccommodationResponseDto getAccommodation(Integer accommodationId);
	List<GetAccommodationResponseDto> getAccommodationList(Integer groupId);
}
