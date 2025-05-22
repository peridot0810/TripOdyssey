package com.ssafy.pjt.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMyRoleInGroupRequestDto {
	@Schema(description="역할을 알고싶은 유저의 아이디")
	private String userId;
	
	@Schema(description="역할을 알고싶은 그룹의 아이디")
	private String groupId;
}
