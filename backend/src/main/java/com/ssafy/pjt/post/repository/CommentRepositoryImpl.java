package com.ssafy.pjt.post.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.pjt.post.dto.request.DeleteCommentRequestDto;
import com.ssafy.pjt.post.dto.request.EditCommentRequestDto;
import com.ssafy.pjt.post.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{
	
	private final CommentMapper commentMapper;

	@Override
	public Integer editComment(EditCommentRequestDto editCommentRequest) {
		return commentMapper.editComment(editCommentRequest);
	}
	
	@Override
	public Integer deleteComment(DeleteCommentRequestDto deleteCommentRequest) {
		return commentMapper.deleteComment(deleteCommentRequest);
	}
	
}
