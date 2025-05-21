package com.ssafy.pjt.transportation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddTransportationRequestDto {
	
	@Schema(description="교통편 타입 아이디 "
			+ "1 : Airplane"
			+ "2 : Train"
			+ "3 : Bus"
			+ "4 : Car"
			+ "5 : Taxi"
			+ "6 : Subway"
			+ "7 : Ferry"
			+ "8 : Bicycle"
			+ "9 : Walking")
	private Integer typeId;
	
	@Schema(description="요청한 그룹 아이디")
	private Integer groupId;
	
	@Schema(description="출발 예정 시간")
	private String departure;
	
	@Schema(description="도착 예정 시간")
	private String arrival;
	
	@Schema(description="출발 위치")
	private String from;
	
	@Schema(description="도착 위치")
	private String to;
	
	@Schema(description="설명")
	private String description;
	
	@Schema(description="생성된 교통편 아이디 (서버가 생성함)", accessMode=Schema.AccessMode.READ_ONLY)
	private Integer transportationId;
}
