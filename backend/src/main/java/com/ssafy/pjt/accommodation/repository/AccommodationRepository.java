package com.ssafy.pjt.accommodation.repository;

import java.util.List;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;


public interface AccommodationRepository {
	void addAccommodation(AddAccommodationRequestDto addAccommodationRequest);
	void updateAccommodation(UpdateAccommodationRequestDto updateAccommodationRequest);
	void deleteAccommodation(Integer accommodationId);
	
	// get
	GetAccommodationResponseDto getAccommodation(Integer accommodationId);
	List<GetAccommodationResponseDto> getAccommodationList(Integer groupId);
}
