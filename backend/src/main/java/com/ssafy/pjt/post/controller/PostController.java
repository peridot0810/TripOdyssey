package com.ssafy.pjt.post.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.pjt.common.dto.response.CommonResponse;
import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.request.EditPostRequestDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;
import com.ssafy.pjt.post.service.PostService;
import com.ssafy.pjt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	
	private final PostService postService;
	private final JwtUtil jwtUtil;
	
	/**
	 * 게시글 생성 
	 * 
	 * @param token
	 * @param createPostRequest
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token,
			@RequestBody CreatePostRequestDto createPostRequest){
		
		// JWT 토큰에서 유저 아이디 추출
		String userId = jwtUtil.extractUserId(token);
		
		Integer postId = postService.createPost(userId, createPostRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 등록에 성공했습니다.", Map.of("postId",postId)));
	}
	
	/**
	 * 게시글 목록 요청
	 * 
	 * @param categoryId
	 * @param page
	 * @param perPage
	 * @param keyword
	 * @param author
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getPostList(
			@RequestParam(required=true) Integer categoryId,
			@RequestParam(required=false, defaultValue="1") Integer page,
			@RequestParam(required=false, defaultValue="5") Integer perPage,
			@RequestParam(required=false, defaultValue="") String keyword,
			@RequestParam(required=false, defaultValue="") String author){
		
		
		List<GetPostResponseDto> postList = postService.getPostList(categoryId, page, perPage, keyword, author);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 목록 조회에 성공했습니다.", postList));
	}
	
	/**
	 * 게시글 상세 요청 
	 * 
	 * @param postId
	 * @return
	 */
	@GetMapping("/{postId}")
	public ResponseEntity<?> getPostDetail(@PathVariable Integer postId){
		GetPostResponseDto postDetail = postService.getPostDetail(postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 상세 조회에 성공했습니다.", postDetail));
	}
	
	/**
	 * 게시글 수정 
	 * 
	 * @param token
	 * @param postId
	 * @param editPostRequest
	 * @return
	 */
	@PutMapping("/{postId}")
	public ResponseEntity<?> editPost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId,
			@RequestBody EditPostRequestDto editPostRequest){
	
		String userId = jwtUtil.extractUserId(token);
		postService.editPost(userId, postId, editPostRequest.getTitle(), editPostRequest.getContent());
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 수정에 성공했습니다.", null));
	}
	
	/**
	 * 게시글 삭제 
	 * 
	 * @param token
	 * @param postId
	 * @return
	 */
	@DeleteMapping("/{postId}")
	public ResponseEntity<?> deletePost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId){
		String userId = jwtUtil.extractUserId(token);
		postService.deletePost(userId, postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 삭제에 성공했습니다.", null));
	}
	
	/**
	 * 게시글 좋아요 누르기 
	 * 
	 * @param token
	 * @param postId
	 * @return
	 */
	@PostMapping("/{postId}/like")
	public ResponseEntity<?> likePost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId){
		String userId = jwtUtil.extractUserId(token);
		postService.likePost(userId, postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 좋아요에 성공했습니다.", null));
	}
	
	/**
	 * 게시글 작성
	 * 
	 * @param token
	 * @param postId
	 * @param content
	 * @return
	 */
	@PostMapping("/{postId}/comments")
	public ResponseEntity<?> commentPost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId,
			@RequestBody String content){
		
		String userId = jwtUtil.extractUserId(token);
		Integer newCommentId = postService.createComment(userId, postId, content);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 작성에 성공했습니다.", Map.of("commentId", newCommentId)));
	}
}
