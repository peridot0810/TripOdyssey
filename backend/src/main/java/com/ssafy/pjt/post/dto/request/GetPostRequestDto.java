package com.ssafy.pjt.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPostRequestDto {
	
	@Schema(description="요청한 유저 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private String userId;
	private Integer postId;
}
