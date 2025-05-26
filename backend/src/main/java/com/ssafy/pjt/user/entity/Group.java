package com.ssafy.pjt.user.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {
	private String groupId;
	private String name;
	private String status;
	private String createdAt;
	private String startDate;
	private String endDate;
	private String profileImage;
}
