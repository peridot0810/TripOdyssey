package com.ssafy.pjt.schedule.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProposalRequestDto {
	private String userId;
	private Integer groupId;
	private Integer attractionsNo;
	private Integer proposalId;
}
