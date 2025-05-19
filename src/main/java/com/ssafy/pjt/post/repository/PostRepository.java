package com.ssafy.pjt.post.repository;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;

public interface PostRepository {
	void createPost(CreatePostRequestDto createPostRequest);
}
