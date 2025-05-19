package com.ssafy.pjt.user.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {
	
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<?> handleDuplicateUser(DuplicateUserException e){
		// 아이디/이메일 중복 처리 
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<?> handleLoginFailed(LoginFailedException e){
		// 로그인 실패 처리 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
	
	@ExceptionHandler(GetUserInfoFailedException.class)
	public ResponseEntity<?> handleGetUserInfoFailed(GetUserInfoFailedException e){
		// 유저 정보 조회 실패 처리
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(EditUserInfoFailedException.class)
	public ResponseEntity<?> handleEditUserInfoFailed(EditUserInfoFailedException e){
		// 유저 정보 수정 실패 처리 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

}
