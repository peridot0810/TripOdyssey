package com.ssafy.pjt.accommodation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateAccommodationRequestDto {
	@Schema(description="수정할 숙소 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer accommodationId;
	
	private String name; 
	private String checkIn;
	private String checkOut;
	private Integer attractionNo;
}
