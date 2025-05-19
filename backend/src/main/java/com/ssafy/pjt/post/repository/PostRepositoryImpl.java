package com.ssafy.pjt.post.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.request.DeletePostRequestDto;
import com.ssafy.pjt.post.dto.request.EditPostRequestDto;
import com.ssafy.pjt.post.dto.request.LikePostRequestDto;
import com.ssafy.pjt.post.dto.request.PostFilterDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;
import com.ssafy.pjt.post.entity.Comment;
import com.ssafy.pjt.post.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository{
	
	private final PostMapper postMapper;
	
	@Override
	public void createPost(CreatePostRequestDto createPostRequest) {
		postMapper.createPost(createPostRequest);
	}
	
	@Override
	public List<GetPostResponseDto> getPostList(PostFilterDto searchCondition) {
		return postMapper.getPostList(searchCondition);
	}
	
	@Override
	public List<Comment> getComments(Integer postId) {
		return postMapper.getComments(postId);
	}
	
	@Override
	public GetPostResponseDto getPost(Integer postId) {
		return postMapper.getPost(postId);
	}
	
	@Override
	public Integer editPost(EditPostRequestDto editPostRequest) {
		return postMapper.editPost(editPostRequest);
	}
	
	@Override
	public Integer deletePost(DeletePostRequestDto deletePostRequest) {
		return postMapper.deletePost(deletePostRequest);
	}
	
	@Override
	public void likePost(LikePostRequestDto likePostRequest) {
		postMapper.likePost(likePostRequest);
	}
}
