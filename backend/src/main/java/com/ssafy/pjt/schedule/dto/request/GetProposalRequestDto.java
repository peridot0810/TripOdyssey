package com.ssafy.pjt.schedule.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProposalRequestDto {
	private Integer groupId;
	private String userId;
}
