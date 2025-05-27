package com.ssafy.pjt.group.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pjt.group.dto.request.UpdateProgressRequestDto;
import com.ssafy.pjt.group.dto.request.UpdateUserExperienceRequestDto;
import com.ssafy.pjt.group.entity.GroupProgress;
import com.ssafy.pjt.group.entity.GroupRoleLimit;
import com.ssafy.pjt.group.entity.TravelGroup;
import com.ssafy.pjt.user.entity.GroupUserInfo2;

@Mapper
public interface TravelGroupMapper {

    void insertTravelGroup(TravelGroup travelGroup);

    void insertGroupRoleLimits(GroupRoleLimit groupRoleLimit);

    Integer getRoleIdByRoleName(String roleName);
    
    void insertGroupProgress(Integer groupId);
    
    void insertUserRole(GroupUserInfo2 groupUserIndo);
    
    int updateTravelGroup(
    		@Param("groupId") Integer groupId,
    		@Param("name") String groupName, 
    		@Param("description") String description,
    		@Param("startDate") LocalDate startDate,
    		@Param("endDate") LocalDate endDate);
    
    int updateGroupRoleLimits(GroupRoleLimit groupRoleLimit);
    
    TravelGroup selectTravelGroupById(Integer groupId);
    List<GroupRoleLimit> selectGroupRoleLimits(Integer groupId);
    GroupProgress selectGroupProgress(Integer groupId);

    void updateGroupImage(Integer groupId, String imgUrl);
    void updateGroupProgress(UpdateProgressRequestDto updateProgressRequest);
    
    void updateUserExperience(UpdateUserExperienceRequestDto updateUserExperienceRequest);
}
