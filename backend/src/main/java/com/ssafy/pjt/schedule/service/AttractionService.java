package com.ssafy.pjt.schedule.service;

import java.util.List;

import com.ssafy.pjt.schedule.dto.request.GetAttractionListRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;

public interface AttractionService {
	
	GetAttractionResponseDto getAttraction(Integer attractionNo);
	List<GetAttractionResponseDto> getAttractionList(Integer sidoCode, Integer gugunCode, Integer contentTypeId, String keyword, Integer page, Integer perPage);
	
}
