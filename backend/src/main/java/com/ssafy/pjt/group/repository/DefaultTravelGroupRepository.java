package com.ssafy.pjt.group.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroup;
import com.ssafy.pjt.group.mapper.TravelGroupMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DefaultTravelGroupRepository implements TravelGroupRepository {
	private final TravelGroupMapper travelGroupMapper;
	
	public void insertTravelGroupAndRoleLimit(TravelGroup travelGroup, RoleLimits roleLimits) {
		travelGroupMapper.insertTravelGroup(travelGroup);
	}
}
