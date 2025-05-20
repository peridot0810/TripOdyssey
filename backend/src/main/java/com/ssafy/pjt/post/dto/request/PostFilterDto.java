package com.ssafy.pjt.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostFilterDto {
	private Integer categoryId;
	private Integer offset;
	private Integer limit;
	private String author;
	private String keyword;
}
