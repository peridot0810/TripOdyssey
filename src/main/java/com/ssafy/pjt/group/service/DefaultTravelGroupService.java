package com.ssafy.pjt.group.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.group.dto.request.TravelGroupPostRequest;
import com.ssafy.pjt.group.dto.response.TravelGroupPostResponse;
import com.ssafy.pjt.group.entity.GroupRoleLimit;
import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroup;
import com.ssafy.pjt.group.entity.TravelGroupStatus;
import com.ssafy.pjt.group.mapper.TravelGroupMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultTravelGroupService implements TravelGroupService {
	
	private final TravelGroupMapper travelGroupMapper;
	
	private final Map<String, Integer> roleIdCache = new ConcurrentHashMap<>();
	
	public CommonResponse<TravelGroupPostResponse> createTravelGroup(TravelGroupPostRequest travelGroupPostRequest){
		
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
		
	    Map<String, Integer> roleLimitsMap = Map.of(
	            "finance", roleLimits.getFinance(),
	            "schedule", roleLimits.getSchedule(),
	            "logistics", roleLimits.getLogistics()
	        );
	    
	    for (Map.Entry<String, Integer> limits : roleLimitsMap.entrySet()) {
	    	
	    	// here add cache check logic, if no cache for this role then query DB
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
	        
	        travelGroupMapper.insertGroupRoleLimit(
	            GroupRoleLimit.builder()
	                .groupId(groupId)
	                .roleId(roleId)
	                .limitCount(limits.getValue())
	                .build()
	        );
	    }
		
	    TravelGroupPostResponse responseData = TravelGroupPostResponse.builder()
	            .groupId(groupId)
	            .name(travelGroup.getName())
	            .description(travelGroup.getDescription())
	            .status(travelGroup.getStatus())
	            .createdAt(travelGroup.getCreatedAt())
	            .startDate(travelGroup.getStartDate())
	            .endDate(travelGroup.getEndDate())
	            .roleLimits(roleLimits)
	            .build();
		
	    return new CommonResponse<TravelGroupPostResponse>(true, "여행 그룹 생성 성공", responseData);
	};
}
