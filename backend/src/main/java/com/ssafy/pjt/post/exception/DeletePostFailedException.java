package com.ssafy.pjt.post.exception;

public class DeletePostFailedException extends RuntimeException{

	private static final long serialVersionUID = 1640884763946394889L;
	public DeletePostFailedException(String message) {
		super(message);
	}

}
