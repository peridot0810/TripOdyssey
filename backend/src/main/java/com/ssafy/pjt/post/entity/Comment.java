package com.ssafy.pjt.post.entity;

import lombok.Data;

@Data
public class Comment {
	private Integer commentId;
	private Integer postId;
	private String content;
	private String writer;
	private String createdAt;
}
