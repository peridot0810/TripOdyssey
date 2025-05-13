package com.ssafy.pjt.user.exception;

public class GetUserInfoFailedException extends RuntimeException{
	private static final long serialVersionUID = 2087890429638008794L;

	public GetUserInfoFailedException(String message) {
		super(message);
	}
}
