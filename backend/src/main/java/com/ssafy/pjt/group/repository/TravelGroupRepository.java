package com.ssafy.pjt.group.repository;

import com.ssafy.pjt.group.entity.RoleLimits;
import com.ssafy.pjt.group.entity.TravelGroup;

public interface TravelGroupRepository {
	
	public void insertTravelGroupAndRoleLimit(TravelGroup travelGroup, RoleLimits roleLimits);
}
