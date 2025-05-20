package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditCommentRequestDto {
	private Integer commentId;
	private String writer;
	private String content;
}
