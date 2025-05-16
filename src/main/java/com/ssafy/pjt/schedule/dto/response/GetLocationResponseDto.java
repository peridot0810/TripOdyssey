package com.ssafy.pjt.schedule.dto.response;

import lombok.Data;

@Data
public class GetLocationResponseDto {
	 private Integer locationId;
     private String title;
     private String imgUrl;
     private String overview;
     // 타입
}
