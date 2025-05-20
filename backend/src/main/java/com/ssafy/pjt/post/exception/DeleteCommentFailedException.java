package com.ssafy.pjt.post.exception;

public class DeleteCommentFailedException extends RuntimeException{

	private static final long serialVersionUID = 9000712533104091729L;
	public DeleteCommentFailedException(String message) {
		super(message);
	}
}
