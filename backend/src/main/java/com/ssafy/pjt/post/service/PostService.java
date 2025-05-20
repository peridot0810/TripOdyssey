package com.ssafy.pjt.post.service;

import java.util.List;

import com.ssafy.pjt.post.dto.request.CreateCommentRequestDto;
import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;

public interface PostService {
	// create
	Integer createPost(String userId, CreatePostRequestDto createPostRequest);
	Integer createComment(String userId, Integer postId, String content);
	
	// get
	List<GetPostResponseDto> getPostList(Integer categoryId, Integer page, Integer perPage, String keyword, String author);
	GetPostResponseDto getPostDetail(Integer postId);
	
	// put
	void editPost(String userId, Integer postId, String title, String content);
	
	// delete
	void deletePost(String userId, Integer postId);
	
	// like
	void likePost(String userId, Integer postId);
}
