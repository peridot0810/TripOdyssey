package com.ssafy.pjt.common.exception;

public class FileUploadIllegalArgumentException extends RuntimeException{
	
	private static final long serialVersionUID = -2622649583972259346L;

	public FileUploadIllegalArgumentException(String message) {
		super(message);
	}
}
