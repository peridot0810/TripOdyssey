package com.ssafy.pjt.schedule.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAttractionListRequestDto {
	private Integer sidoCode;
	private Integer gugunCode;
	private Integer contentTypeId;
	private String keyword;
	private Integer limit;
	private Integer offset;
}
