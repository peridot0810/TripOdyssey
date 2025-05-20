package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentRequestDto {
	private String writer;
	private Integer postId;
	private Integer commentId;
	private String content;
}
