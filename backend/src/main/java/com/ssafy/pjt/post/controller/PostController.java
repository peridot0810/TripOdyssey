package com.ssafy.pjt.post.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final JwtUtil jwtUtil;

	@Operation(summary="게시글 생성", description="새로운 게시글을 생성합니다.")
	@ApiResponse(responseCode = "200", description="게시글 생성 성공")
	@PostMapping
	public ResponseEntity<?> createPost(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody CreatePostRequestDto createPostRequest) {
		String userId = userDetails.getUsername();
		Integer postId = postService.createPost(userId, createPostRequest);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 등록에 성공했습니다.", Map.of("postId", postId)));
	}

	@Operation(summary="게시글 목록 조회", description="카테고리, 키워드, 작성자 등을 기준으로 게시글 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description="게시글 목록 조회 성공")
	@GetMapping
	public ResponseEntity<?> getPostList(
			@RequestParam(required = true) Integer categoryId,
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "5") Integer perPage,
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(required = false, defaultValue = "") String author) {
		List<GetPostResponseDto> postList = postService.getPostList(categoryId, page, perPage, keyword, author);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 목록 조회에 성공했습니다.", postList));
	}

	@Operation(summary="게시글 상세 조회", description="게시글 ID로 상세 내용을 조회합니다.")
	@ApiResponse(responseCode = "200", description="게시글 상세 조회 성공")
	@GetMapping("/{postId}")
	public ResponseEntity<?> getPostDetail(@PathVariable Integer postId) {
		GetPostResponseDto postDetail = postService.getPostDetail(postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 상세 조회에 성공했습니다.", postDetail));
	}

	@Operation(summary="게시글 수정", description="게시글 ID로 특정 게시글을 수정합니다.")
	@ApiResponse(responseCode = "200", description="게시글 수정 성공")
	@PutMapping("/{postId}")
	public ResponseEntity<?> editPost(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer postId,
			@RequestBody EditPostRequestDto editPostRequest) {
		String userId = userDetails.getUsername();
		postService.editPost(userId, postId, editPostRequest.getTitle(), editPostRequest.getContent());
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 수정에 성공했습니다.", null));
	}

	@Operation(summary="게시글 삭제", description="게시글 ID로 특정 게시글을 삭제합니다.")
	@ApiResponse(responseCode = "200", description="게시글 삭제 성공")
	@DeleteMapping("/{postId}")
	public ResponseEntity<?> deletePost(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer postId) {
		String userId = userDetails.getUsername();
		postService.deletePost(userId, postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 삭제에 성공했습니다.", null));
	}

	@Operation(summary="게시글 좋아요", description="게시글에 좋아요를 누릅니다.")
	@ApiResponse(responseCode = "200", description="게시글 좋아요 성공")
	@PostMapping("/{postId}/like")
	public ResponseEntity<?> likePost(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer postId) {
		String userId = userDetails.getUsername();
		postService.likePost(userId, postId);
		return ResponseEntity.ok(new CommonResponse<>(true, "게시글 좋아요에 성공했습니다.", null));
	}

	@Operation(summary="게시글 댓글 작성", description="게시글에 댓글을 작성합니다.")
	@ApiResponse(responseCode = "200", description="댓글 작성 성공")
	@PostMapping("/{postId}/comments")
	public ResponseEntity<?> commentPost(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer postId,
			@RequestBody String content) {
		String userId = userDetails.getUsername();
		Integer newCommentId = postService.createComment(userId, postId, content);
		return ResponseEntity.ok(new CommonResponse<>(true, "댓글 작성에 성공했습니다.", Map.of("commentId", newCommentId)));
	}
}

