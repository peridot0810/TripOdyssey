package com.ssafy.pjt.common.exception;


public class UserNotInGroupException extends RuntimeException {
	private static final long serialVersionUID = 3833542795892930192L;

	public UserNotInGroupException(String message) {
		super(message);
	}
}
