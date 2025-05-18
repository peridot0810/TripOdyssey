package com.ssafy.pjt.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;

@Mapper
public interface PostMapper {
	void createPost(CreatePostRequestDto createPostRequest);
}
