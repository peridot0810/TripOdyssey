package com.ssafy.pjt.post.exception;

public class EditCommentFailedException extends RuntimeException{

	private static final long serialVersionUID = 9000712533104091729L;
	public EditCommentFailedException(String message) {
		super(message);
	}
}
