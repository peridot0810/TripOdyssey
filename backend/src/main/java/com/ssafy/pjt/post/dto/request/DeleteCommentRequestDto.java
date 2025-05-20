package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCommentRequestDto {
	private String userId;
	private Integer commentId;
}
