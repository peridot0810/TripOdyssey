package com.ssafy.pjt.group.dto.response;

import groovy.transform.builder.Builder;
import lombok.Data;

@Data
@Builder
public class InvitedMemberResponseDto {
	private Integer invitationId;
	private String receiverId;
	private String receiverNickname;
	private String status;
	private String createdAt;
	private String respondedAt;
}
