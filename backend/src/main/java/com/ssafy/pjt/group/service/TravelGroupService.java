package com.ssafy.pjt.group.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.request.TravelGroupUpdateRequest;
import com.ssafy.pjt.group.dto.request.UpdateProgressRequestDto;
import com.ssafy.pjt.group.dto.response.TravelGroupInfoResponse;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;

public interface TravelGroupService {
	public CommonResponse<TravelGroupPostResponse> createTravelGroup(TravelGroupPostRequest travelGroupPostRequest, String userId);
	public CommonResponse<Void> updateTravelGroup(TravelGroupUpdateRequest travelGroupUpdateRequest);
	public CommonResponse<TravelGroupInfoResponse> getTravelGroupInfo(Integer groupId);
	
	public String uploadImage(Integer groupId,MultipartFile file) throws IOException;
	public void updateProgress(Integer groupId, UpdateProgressRequestDto updateProgressRequest);
}
