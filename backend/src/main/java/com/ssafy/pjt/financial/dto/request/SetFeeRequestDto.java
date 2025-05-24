package com.ssafy.pjt.financial.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SetFeeRequestDto {
	
	@Schema(description="회비 설정할 그룹 아이디", accessMode=Schema.AccessMode.READ_ONLY)
	Integer groupId;
	
	
	Integer budget;
}
