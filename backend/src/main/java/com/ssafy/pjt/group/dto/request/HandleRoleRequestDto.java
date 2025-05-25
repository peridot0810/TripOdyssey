package com.ssafy.pjt.group.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HandleRoleRequestDto {
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private String userId;
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private Integer groupId;
	
	private Integer roleId;
	private Boolean accept;
}
