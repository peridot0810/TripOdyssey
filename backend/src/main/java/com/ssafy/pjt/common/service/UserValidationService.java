package com.ssafy.pjt.common.service;

public interface UserValidationService {
	//public boolean isUserInGroup(String userId, Integer groupId);
	
	public boolean isUserRoleValid(String userId, Integer groupId, Integer roleId);
	public boolean isUserInGroup(String userId, Integer groupId);
	public Boolean[] getUserRoles(String userId, Integer groupId);
}
