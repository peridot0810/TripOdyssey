package com.ssafy.pjt.post.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.post.dto.request.CreateCommentRequestDto;
import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.request.DeletePostRequestDto;
import com.ssafy.pjt.post.dto.request.EditPostRequestDto;
import com.ssafy.pjt.post.dto.request.LikePostRequestDto;
import com.ssafy.pjt.post.dto.request.PostFilterDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;
import com.ssafy.pjt.post.exception.DeletePostFailedException;
import com.ssafy.pjt.post.exception.EditPostFailedException;
import com.ssafy.pjt.post.exception.LikePostFailedException;
import com.ssafy.pjt.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepository;
	
	@Override
	public Integer createPost(String userId, CreatePostRequestDto createPostRequest) {
		
		createPostRequest.setAuthor(userId);
		postRepository.createPost(createPostRequest);
		return createPostRequest.getPostId();
	}
	
	@Override
	public List<GetPostResponseDto> getPostList(Integer categoryId, Integer page, Integer perPage,
			String keyword, String author) {

		// 값 세팅
		PostFilterDto filter = PostFilterDto.builder()
											.categoryId(categoryId)
											.limit(perPage)
											.offset((page-1) * perPage)
											.keyword(keyword)
											.author(author)
											.build();
		
		return postRepository.getPostList(filter);
	}
	
	@Override
	public GetPostResponseDto getPostDetail(Integer postId) {
		
		// 게시글 정보 가져오기 
		GetPostResponseDto post = postRepository.getPost(postId);
		
		// 댓글 정보 가져오기 
		post.setCommentList(postRepository.getComments(postId));
		
		return post;
	}
	
	@Override
	public void editPost(String userId, Integer postId, String title, String content) {
		
		// 게시글 수정
		Integer editedPostCnt = postRepository.editPost(EditPostRequestDto.builder()
																		.userId(userId)
																		.postId(postId)
																		.title(title)
																		.content(content)
																		.build());
		// 수정된 게시글이 없다면 예외 던지기
		if(editedPostCnt == 0) {
			throw new EditPostFailedException("수정할 게시글이 없거나, 수정 권한이 없습니다.");
		}
	}
	
	@Override
	public void deletePost(String userId, Integer postId) {
		
		// 게시글 삭제
		Integer deletedPostCnt = postRepository.deletePost(DeletePostRequestDto.builder()
				.userId(userId)
				.postId(postId)
				.build());

		// 삭제된 게시글이 없다면 예외 던지기
		if(deletedPostCnt == 0) {
			throw new DeletePostFailedException("삭제할 게시글이 없거나, 수정 권한이 없습니다.");
		}
	}
	
	@Override
	public void likePost(String userId, Integer postId) {
		
		try {
			// 좋아요 누르기 
			postRepository.likePost(LikePostRequestDto.builder()
					.userId(userId)
					.postId(postId)
					.build());
		} catch (DuplicateKeyException e) {
			throw new LikePostFailedException("이미 좋아요를 누른 게시글입니다.");
		}
	}
	
	@Override
	public Integer createComment(String userId, Integer postId, String content) {
		
		CreateCommentRequestDto newComment = CreateCommentRequestDto.builder()
																	.writer(userId)
																	.postId(postId)
																	.content(content)
																	.build(); 
		postRepository.createComment(newComment);
		return newComment.getCommentId();
	}
}
