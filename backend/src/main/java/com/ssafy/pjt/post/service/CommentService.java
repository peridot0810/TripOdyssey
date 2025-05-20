package com.ssafy.pjt.post.service;

public interface CommentService {
	void editComment(String userId, String content, Integer commentId);
	void deleteComment(String userId, Integer commentId);
}
