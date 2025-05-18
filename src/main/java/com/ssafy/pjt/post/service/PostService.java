package com.ssafy.pjt.post.service;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;

public interface PostService {
	Integer createPost(String userId, CreatePostRequestDto createPostRequest);
}
