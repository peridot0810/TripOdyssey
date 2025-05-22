package com.ssafy.pjt.accommodation.service;

import java.util.List;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;

public interface AccommodationService {
	Integer addAccommodation(String userId, Integer groupId, AddAccommodationRequestDto addAccommodationRequest);
	void updateAccommodation(String userId, Integer groupId, Integer accommodationId, UpdateAccommodationRequestDto updateAccommodationRequest);
	void deleteAccommodation(String userId, Integer groupId, Integer accommodationId);
	
	// get
	GetAccommodationResponseDto getAccommodation(String userId, Integer groupId, Integer accommodationId);
	List<GetAccommodationResponseDto> getAccommodationList(String userId, Integer groupId);
}
