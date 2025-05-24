package com.ssafy.pjt.financial.dto.request;

import com.github.victools.jsonschema.generator.SchemaKeyword;

import groovy.transform.builder.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Builder
public class MemberInviteRequestDto {

	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private Integer groupId;
	
	@Schema(accessMode=Schema.AccessMode.READ_ONLY)
	private String senderId;
	
	
	private String receiverId;
}
