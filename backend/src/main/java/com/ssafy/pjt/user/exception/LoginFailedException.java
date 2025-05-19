package com.ssafy.pjt.user.exception;

public class LoginFailedException extends RuntimeException{
	private static final long serialVersionUID = 6552102537785978648L;

	public LoginFailedException(String message) {
		super(message);
	}
}
