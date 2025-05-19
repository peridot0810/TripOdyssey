package com.ssafy.pjt.group.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.common.entity.MemberRole;
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

@Service
@RequiredArgsConstructor
public class DefaultTravelGroupService implements TravelGroupService {
	
	private final TravelGroupMapper travelGroupMapper;
	
	private final Map<String, Integer> roleIdCache = new ConcurrentHashMap<>();
	
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
		
		travelGroupMapper.insertTravelGroup(travelGroup);
		// 이제 그룹 아이디가 생성됨
		int groupId = travelGroup.getGroupId();
		
		// get role limits
		RoleLimits roleLimits = travelGroupPostRequest.getRoleLimits();
		
		saveRoleLimits(groupId, roleLimits, true);
	    
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

	
}
