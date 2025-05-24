package com.ssafy.pjt.user.dto.response;

import lombok.Data;

@Data
public class InvitationResponseDto {

	private Integer invitationId;
	private Integer groupId;
	private String groupName;
	private String senderId;
	private String senderNickname;
	private String status;
	private String createdAt;
	private String respondedAt;
}
