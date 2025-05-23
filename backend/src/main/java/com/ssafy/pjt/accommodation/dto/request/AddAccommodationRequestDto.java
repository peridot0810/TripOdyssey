package com.ssafy.pjt.accommodation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAccommodationRequestDto {
	@Schema(description="요청 그룹 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer groupId;
	
	@Schema(description="새로 생성된 숙소 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer accommodationId;
	
	
	private String name;
	private Integer attractionNo;
	
	@NotNull
	private String checkInTime;
	@NotNull
	private String checkOutTime;
}
