package com.ssafy.pjt.post.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PostExceptionHandler {

	@ExceptionHandler(EditPostFailedException.class)
	public ResponseEntity<?> handleEditPostFailed(EditPostFailedException e){
		// 게시글 수정 실패 예외 처리 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(DeletePostFailedException.class)
	public ResponseEntity<?> handleDeletePostFailed(DeletePostFailedException e){
		// 게시글 삭제 실패 예외 처리
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(LikePostFailedException.class)
	public ResponseEntity<?> handleLikePostFailed(LikePostFailedException e){
		// 게시글 좋아요 실패 예외 처리
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(EditCommentFailedException.class)
	public ResponseEntity<?> handleEditCommentFailed(EditCommentFailedException e){
		// 댓글 수정 실패 예외 처리 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(DeleteCommentFailedException.class)
	public ResponseEntity<?> handleDeleteCommentFailed(DeleteCommentFailedException e){
		// 댓글 삭제 실패 예외 처리 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
