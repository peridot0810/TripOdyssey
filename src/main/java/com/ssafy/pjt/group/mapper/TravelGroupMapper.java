package com.ssafy.pjt.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.group.entity.GroupRoleLimit;
import com.ssafy.pjt.group.entity.TravelGroup;

@Mapper
public interface TravelGroupMapper {

    void insertTravelGroup(TravelGroup travelGroup);

    void insertGroupRoleLimit(GroupRoleLimit groupRoleLimit);

    Integer getRoleIdByRoleName(String roleName);
}
