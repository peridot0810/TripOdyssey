package com.ssafy.pjt.group.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.FileUploadIllegalArgumentException;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.request.TravelGroupUpdateRequest;
import com.ssafy.pjt.group.dto.response.TravelGroupInfoResponse;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.entity.GroupRoleLimit;
import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroup;
import com.ssafy.pjt.group.entity.TravelGroupStatus;
import com.ssafy.pjt.group.mapper.TravelGroupMapper;
import com.ssafy.pjt.user.entity.GroupUserInfo2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultTravelGroupService implements TravelGroupService {
	
	private final TravelGroupMapper travelGroupMapper;
	
	private final Map<String, Integer> roleIdCache = new ConcurrentHashMap<>();
	
	private final List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
	
	@Value("${spring.servlet.multipart.location}")
	private String IMG_UPLOAD_DIR;
	
	@Value("${group.img.get-url-prefix}")
	private String GROUP_IMG_PATH_PREFIX;
	
	public CommonResponse<TravelGroupPostResponse> createTravelGroup(TravelGroupPostRequest travelGroupPostRequest, String userId){
		
		// group id 없는 그룹 객체 생성
		TravelGroup travelGroup = TravelGroup.builder()
				.name(travelGroupPostRequest.getName())
				.description(travelGroupPostRequest.getDescription())
				.status(TravelGroupStatus.planning) // 새로운 그룹이라서 planning 단계
				.createdAt(LocalDateTime.now())		// 현재 시간으로 생성됨
				.startDate(travelGroupPostRequest.getStartDate())
				.endDate(travelGroupPostRequest.getEndDate())
				.build();
		
		
		// ------Transaction--------
		travelGroupMapper.insertTravelGroup(travelGroup);
		// 이제 그룹 아이디가 생성됨
		int groupId = travelGroup.getGroupId();
		
		// 그룹 진척도 데이터 추가
		travelGroupMapper.insertGroupProgress(groupId);
		
		// 인원 제한 설정 
		// get role limits
		RoleLimits roleLimits = travelGroupPostRequest.getRoleLimits();
		
		saveRoleLimits(groupId, roleLimits, true);
		
		// -------------------------
		
		
	    // 그룹을 생성한 유저를 해당 그룹의 방장으로 설정
	    GroupUserInfo2 groupUserInfo = GroupUserInfo2.builder()
	    		.userId(userId)
	    		.groupId(groupId)
	    		.roleId(MemberRole.MASTER.getId())
	    		.build();
	    
	    travelGroupMapper.insertUserRole(groupUserInfo);
		
	    TravelGroupPostResponse responseData = TravelGroupPostResponse.builder()
	            .groupId(groupId)
	            .status(travelGroup.getStatus())
	            .createdAt(travelGroup.getCreatedAt())
	            .build();
		
	    return new CommonResponse<TravelGroupPostResponse>(true, "여행 그룹 생성 성공", responseData);
	};
	
	public CommonResponse<Void> updateTravelGroup(TravelGroupUpdateRequest updateRequest){
		travelGroupMapper.updateTravelGroup(
				updateRequest.getGroupId(),
				updateRequest.getName(), 
				updateRequest.getDescription(), 
				updateRequest.getStartDate(), 
				updateRequest.getEndDate());
		
		saveRoleLimits(updateRequest.getGroupId(), updateRequest.getRoleLimits(), false);
		
		return new CommonResponse<Void>(true, "여행 그룹 정보 수정 성공", null);
	}
	
	private void saveRoleLimits(int groupId, RoleLimits roleLimits, boolean isCreate) {
	    Map<String, Integer> roleLimitsMap = Map.of(
	            "finance", roleLimits.getFinance(),
	            "schedule", roleLimits.getSchedule(),
	            "logistics", roleLimits.getLogistics()
	    );

	    for (Map.Entry<String, Integer> limits : roleLimitsMap.entrySet()) {
	        String roleName = limits.getKey();

	        if (roleIdCache.containsKey(roleName)) {
	            System.out.println("Cache hit for role: " + roleName);
	        }

	        int roleId = roleIdCache.computeIfAbsent(
	                roleName,
	                name -> {
	                    System.out.println("Cache miss for role: " + name + ", querying DB...");
	                    return travelGroupMapper.getRoleIdByRoleName(name);
	                }
	        );

	        GroupRoleLimit roleLimit = GroupRoleLimit.builder()
	                .groupId(groupId)
	                .roleId(roleId)
	                .limitCount(limits.getValue())
	                .build();

	        if (isCreate) {
	            travelGroupMapper.insertGroupRoleLimits(roleLimit);
	        } else {
	            travelGroupMapper.updateGroupRoleLimits(roleLimit);
	        }
	    }
	}

	@Override
	public CommonResponse<TravelGroupInfoResponse> getTravelGroupInfo(Integer groupId) {
		TravelGroup group = travelGroupMapper.selectTravelGroupById(groupId);
		List<GroupRoleLimit> limitList = travelGroupMapper.selectGroupRoleLimits(groupId);

		RoleLimits roleLimits = new RoleLimits();
		for (GroupRoleLimit r : limitList) {
		    switch (r.getRoleId()) {
		        case 2 -> roleLimits.setSchedule(r.getLimitCount());
		        case 3 -> roleLimits.setFinance(r.getLimitCount());
		        case 4 -> roleLimits.setLogistics(r.getLimitCount());
		    }
		}
		
		TravelGroupInfoResponse groupInfo = TravelGroupInfoResponse.builder()
				.groupId(groupId)
				.name(group.getName())
				.description(group.getDescription())
				.status(group.getStatus())
				.createdAt(group.getCreatedAt())
				.startDate(group.getStartDate())
				.endDate(group.getEndDate())
				.roleLimits(roleLimits)
				.build();
		
		return new CommonResponse<TravelGroupInfoResponse>(true, "그룹 조회 성공", groupInfo);
	}

	
	@Override
	public String uploadImage(Integer groupId, MultipartFile file) throws IOException {

		// 저장 경로 설정
		File dir = new File(IMG_UPLOAD_DIR+GROUP_IMG_PATH_PREFIX);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		// 파일 이름 생성 및 검증 
		String originalFileName = file.getOriginalFilename();
		if (originalFileName == null || originalFileName.trim().isEmpty()) {
	        throw new FileUploadIllegalArgumentException("파일 이름이 유효하지 않습니다.");
	    }
		
		// 파일 확장자 추출 및 검증 
		String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
		if (!allowedExtensions.contains(extension)) {
	        throw new FileUploadIllegalArgumentException("허용되지 않는 파일 확장자입니다: " + extension);
	    }
		
		// 저장 파일명 생성
	    String baseName = originalFileName.substring(0, originalFileName.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9]", "_");
	    String savedFileName = UUID.randomUUID() + "_" + baseName + extension;
		
		log.debug("original file name : {}", originalFileName);
		log.debug("extension : {}", extension);
		log.debug("savedFileName : {}", savedFileName);
		
		// 실제 파일 저장
		Path filePath = Paths.get(IMG_UPLOAD_DIR+GROUP_IMG_PATH_PREFIX, savedFileName);
		file.transferTo(filePath.toFile());
		
		// 로깅 
		log.debug("Uploaded file: {}, Saved as: {}", originalFileName, savedFileName);

		
		// 접근 경로 생성
		String imageUrl = GROUP_IMG_PATH_PREFIX + savedFileName;
		
		// DB에 이미지 경로 저장 
		travelGroupMapper.updateGroupImage(groupId, imageUrl);
		
		return imageUrl;
	}
	
}
