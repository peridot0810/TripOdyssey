package com.ssafy.pjt.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddContentRequestDto {
	
	@Schema(description="새로운 일정카드 아이디 (서버가 생성함)", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer contentId;	
	
	@Schema(description="새로운 일정카드 이름")
	private String name;
	
	@Schema(description="일정 설명")
	private String description;
	
	@Schema(description="일정에 포함되는 장소 아이디")
	private Integer attractionNo;
}
