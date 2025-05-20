package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditPostRequestDto {
	private String userId;
	private Integer postId;
	private String title;
	private String content;
}
