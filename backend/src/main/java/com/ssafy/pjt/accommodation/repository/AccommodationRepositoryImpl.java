package com.ssafy.pjt.accommodation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.accommodation.dto.request.AddAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.request.UpdateAccommodationRequestDto;
import com.ssafy.pjt.accommodation.dto.response.GetAccommodationResponseDto;
import com.ssafy.pjt.accommodation.mapper.AccommodationMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepository{

	private final AccommodationMapper accommodationMapper;
	
	@Override
	public void addAccommodation(AddAccommodationRequestDto addAccommodationRequest) {
		accommodationMapper.addAccommodation(addAccommodationRequest);
	}
	
	@Override
	public void updateAccommodation(UpdateAccommodationRequestDto updateAccommodationRequest) {
		accommodationMapper.updateAccommodation(updateAccommodationRequest);
	}
	
	@Override
	public void deleteAccommodation(Integer accommodationId) {
		accommodationMapper.deleteAccommodation(accommodationId);
	}
	
	@Override
	public GetAccommodationResponseDto getAccommodation(Integer accommodationId) {
		return accommodationMapper.getAccommodation(accommodationId);
	}
	
	@Override
	public List<GetAccommodationResponseDto> getAccommodationList(Integer groupId) {
		return accommodationMapper.getAccommodationList(groupId);
	}
}
