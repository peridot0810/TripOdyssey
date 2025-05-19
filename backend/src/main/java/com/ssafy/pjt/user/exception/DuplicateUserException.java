package com.ssafy.pjt.user.exception;

public class DuplicateUserException extends RuntimeException {
    private static final long serialVersionUID = -3356469510025254593L;

	public DuplicateUserException(String message) {
        super(message);
    }
}