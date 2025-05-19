package com.ssafy.pjt.post.service;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
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
}
