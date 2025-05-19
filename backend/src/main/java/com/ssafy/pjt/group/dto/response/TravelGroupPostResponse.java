package com.ssafy.pjt.group.dto.response;

import java.time.LocalDateTime;
import com.ssafy.pjt.group.entity.TravelGroupStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelGroupPostResponse {
	private Integer groupId;
	private TravelGroupStatus status;
	private LocalDateTime createdAt;
}
