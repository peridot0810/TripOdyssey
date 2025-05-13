package com.ssafy.pjt.user.exception;

public class EditUserInfoFailedException extends RuntimeException{
	private static final long serialVersionUID = -7480591912870823287L;

	public EditUserInfoFailedException(String message) {
		super(message);
	}
}
