package com.ssafy.pjt.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.schedule.dto.request.GetAttractionListRequestDto;
import com.ssafy.pjt.schedule.dto.response.GetAttractionResponseDto;
import com.ssafy.pjt.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService{
	private final ScheduleRepository scheduleRepository;
	
	@Override
	public GetAttractionResponseDto getAttraction(Integer attractionNo) {
		
		// 비즈니스 로직
		return scheduleRepository.getAttractionInfo(attractionNo);
	}
	
	@Override
	public List<GetAttractionResponseDto> getAttractionList(Integer sidoCode, Integer gugunCode, Integer contentTypeId, String keyword, Integer page, Integer perPage) {
		
		
		// 비즈니스 로직
		// 값 세팅
		GetAttractionListRequestDto searchCondition = GetAttractionListRequestDto.builder()
				.sidoCode(sidoCode)
				.gugunCode(gugunCode)
				.contentTypeId(contentTypeId)
				.keyword(keyword)
				.limit(perPage)
				.offset((page-1) * perPage)
				.build();
		
		return scheduleRepository.getAttractionList(searchCondition);
	}
}
