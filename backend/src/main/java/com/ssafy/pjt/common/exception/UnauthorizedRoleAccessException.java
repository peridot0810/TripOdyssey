package com.ssafy.pjt.common.exception;

public class UnauthorizedRoleAccessException extends RuntimeException{
	private static final long serialVersionUID = 6856243486389236681L;

	public UnauthorizedRoleAccessException(String message) {
		super(message);
	}
}
