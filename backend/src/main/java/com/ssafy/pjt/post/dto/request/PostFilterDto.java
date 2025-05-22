package com.ssafy.pjt.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
	
	@Schema(description="게시글 목록 조회 요청한 유저 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private String userId;
}
