package com.ssafy.pjt.post.repository;

import java.util.List;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.request.DeletePostRequestDto;
import com.ssafy.pjt.post.dto.request.EditPostRequestDto;
import com.ssafy.pjt.post.dto.request.LikePostRequestDto;
import com.ssafy.pjt.post.dto.request.PostFilterDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;
import com.ssafy.pjt.post.entity.Comment;

public interface PostRepository {
	// create
	void createPost(CreatePostRequestDto createPostRequest);
	
	// get
	List<GetPostResponseDto> getPostList(PostFilterDto searchCondition);
	GetPostResponseDto getPost(Integer postId);
	List<Comment> getComments(Integer postId);
	
	// put
	Integer editPost(EditPostRequestDto editPostRequest);
	
	// delete
	Integer deletePost(DeletePostRequestDto deletePostRequest);
	
	// like
	void likePost(LikePostRequestDto likePostRequest);
}
