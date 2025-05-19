package com.ssafy.pjt.post.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.service.PostService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	
	private final PostService postService;
	private final JwtUtil jwtUtil;
	
	
	@PostMapping
	public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token,
			@RequestBody CreatePostRequestDto createPostRequest){
		
		// JWT 토큰에서 유저 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		Integer postId = postService.createPost(userId, createPostRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 등록에 성공했습니다.", Map.of("postId",postId)));
	}
	
}
