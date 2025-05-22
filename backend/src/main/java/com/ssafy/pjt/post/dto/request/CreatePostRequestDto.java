package com.ssafy.pjt.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequestDto {
	
	@Schema(description="게시글 작성자", accessMode=Schema.AccessMode.READ_ONLY)
	private String author;
	private String title;
	private String content;
	
	@Schema(description="생성된 게시글 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer postId;
	private Integer categoryId;
}
