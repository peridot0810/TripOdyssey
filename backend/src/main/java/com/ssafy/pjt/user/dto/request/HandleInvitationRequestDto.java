package com.ssafy.pjt.user.dto.request;

import groovy.transform.builder.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Builder
public class HandleInvitationRequestDto {
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private String receiverEmail;
	
	private String senderId;
	private Integer groupId;
	private Boolean accept;
}
