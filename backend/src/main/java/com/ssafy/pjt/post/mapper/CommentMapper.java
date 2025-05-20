package com.ssafy.pjt.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.post.dto.request.DeleteCommentRequestDto;
import com.ssafy.pjt.post.dto.request.EditCommentRequestDto;

@Mapper
public interface CommentMapper {

	Integer editComment(EditCommentRequestDto editCommentRequest);
	Integer deleteComment(DeleteCommentRequestDto deleteCommentRequest);
}
