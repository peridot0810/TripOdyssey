package com.ssafy.pjt.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.pjt.common.dto.response.CommonResponse;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandler {

	@ExceptionHandler(UserNotInGroupException.class)
	public ResponseEntity<?> handleUserNotInGroup(UserNotInGroupException e){
		// 속하지 않은 그룹의 정보 요청 처리 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse<>(false, e.getMessage(), null));
	}
	
	@ExceptionHandler(UnauthorizedRoleAccessException.class)
	public ResponseEntity<?> handleUnauthorizedRoleAccess(UnauthorizedRoleAccessException e){
		// 주어진 역할로는 불가능한 요청 처리
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse<>(false, e.getMessage(), null));
	}
	
	@ExceptionHandler(FileUploadIllegalArgumentException.class)
	public ResponseEntity<?> handleFileUploadIllegalArgument(FileUploadIllegalArgumentException e){
		// 유효하지 않은 형식의 파일 업로드 요청 처리
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(false, e.getMessage(), null));
	}
}
