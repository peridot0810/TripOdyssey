package com.ssafy.pjt.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.post.service.CommentService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/{commentId}")
public class CommentController {
	private final CommentService commentService;
	private final JwtUtil jwtUtil;
	
	/**
	 * 댓글 수정 
	 * 
	 * @param token
	 * @param commentId
	 * @param content
	 * @return
	 */
	@PutMapping
	public ResponseEntity<?> editComment(@RequestHeader("Authorization") String token,
		@PathVariable Integer commentId,
		@RequestBody String content){
		
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		commentService.editComment(userId, content, commentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 수정에 성공했습니다.", null));
	}
	
	/**
	 * 댓글 삭제 
	 * 
	 * @param token
	 * @param commentId
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token,
		@PathVariable Integer commentId){
		// JWT 토큰에서 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		commentService.deleteComment(userId, commentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 삭제에 성공했습니다.", null));
	}
}
