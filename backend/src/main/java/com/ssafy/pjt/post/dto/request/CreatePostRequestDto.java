package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequestDto {
	private String author;
	private String title;
	private String content;
	private Integer postId;
	private Integer categoryId;
}
