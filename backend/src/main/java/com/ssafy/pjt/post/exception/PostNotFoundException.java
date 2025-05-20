package com.ssafy.pjt.post.exception;


public class PostNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2074670999283629489L;
	public PostNotFoundException(String message) {
		super(message);
	}
}
