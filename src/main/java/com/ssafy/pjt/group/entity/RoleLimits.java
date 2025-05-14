package com.ssafy.pjt.group.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleLimits {
	private Integer finance;
	private Integer schedule;
	private Integer logistics;
}
