package com.ssafy.pjt.post.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
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
}
