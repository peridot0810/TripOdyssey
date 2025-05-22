package com.ssafy.pjt.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditPostRequestDto {
	
	@Schema(description="수정을 요청한 유저 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private String userId;
	
	@Schema(description="수정을 요청한 게시글 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer postId;
	private String title;
	private String content;
}
