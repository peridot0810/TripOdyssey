package com.ssafy.pjt.group.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleLimits {
	private Integer finance;
	private Integer schedule;
	private Integer logistics;
}
