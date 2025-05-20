package com.ssafy.pjt.post.exception;

public class EditPostFailedException extends RuntimeException{
	private static final long serialVersionUID = 632437959807721735L;

	public EditPostFailedException(String message) {
		super(message);
	}
}
