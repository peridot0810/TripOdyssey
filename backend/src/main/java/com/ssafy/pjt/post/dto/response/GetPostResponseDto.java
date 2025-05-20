package com.ssafy.pjt.post.dto.response;

import java.util.List;

import com.ssafy.pjt.post.entity.Comment;

import lombok.Data;

@Data
public class GetPostResponseDto {
	private Integer postId;
	private String title;
	private String content;
	private Integer categoryId;
	private String categoryName;
	private String createdAt;
	private Integer views;
	private Integer likes;
	private String author;
	
	private List<Comment> commentList;
}
