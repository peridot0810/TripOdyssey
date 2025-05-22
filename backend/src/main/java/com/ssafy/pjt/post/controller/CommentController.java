package com.ssafy.pjt.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.post.service.CommentService;
import com.ssafy.pjt.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/{commentId}")
public class CommentController {
	private final CommentService commentService;
	private final JwtUtil jwtUtil;
	
	
	@Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
	@ApiResponse(responseCode = "200", description = "댓글 수정 성공")
	@PutMapping
	public ResponseEntity<?> editComment(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer commentId,
			@RequestBody String content){

		String userId = userDetails.getUsername();
		commentService.editComment(userId, content, commentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 수정에 성공했습니다.", null));
	}
	
	
	@Operation(summary = "댓글 삭제", description = "해당 댓글을 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
	@DeleteMapping
	public ResponseEntity<?> deleteComment(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer commentId){

		String userId = userDetails.getUsername();
		commentService.deleteComment(userId, commentId);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 삭제에 성공했습니다.", null));
	}
}