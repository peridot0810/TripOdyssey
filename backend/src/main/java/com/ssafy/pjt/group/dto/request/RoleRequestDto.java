package com.ssafy.pjt.group.dto.request;

import groovy.transform.builder.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Builder
public class RoleRequestDto {
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private String userId;
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private Integer groupId;
	
	private Integer roleId;
}
