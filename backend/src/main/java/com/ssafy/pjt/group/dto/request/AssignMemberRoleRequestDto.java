package com.ssafy.pjt.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AssignMemberRoleRequestDto {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String userId;
	
	private Integer[] assignRoleIdList;
	private Integer[] removalRoleIdList;
	
}
