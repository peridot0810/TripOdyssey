package com.ssafy.pjt.group.service;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.entity.TravelGroup;

public interface TravelGroupService {
	public CommonResponse<TravelGroupPostResponse> createTravelGroup(TravelGroupPostRequest travelGroupPostRequest);
}
