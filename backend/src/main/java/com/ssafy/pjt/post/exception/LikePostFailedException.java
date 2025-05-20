package com.ssafy.pjt.post.exception;

public class LikePostFailedException extends RuntimeException{

	private static final long serialVersionUID = 5743481187186341098L;
	public LikePostFailedException(String message) {
		super(message);
	}
}
