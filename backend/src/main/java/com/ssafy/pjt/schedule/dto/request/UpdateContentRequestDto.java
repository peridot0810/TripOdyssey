package com.ssafy.pjt.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateContentRequestDto {
	
	@Schema(description="수정할 일정카드 아이디")
	private Integer contentId;	
	
	@Schema(description="수정할 이름")
	private String name;
	
	@Schema(description="수정할 설명")
	private String description;
	
	@Schema(description="수정할 장소 no")
	private Integer attractionNo;
}
