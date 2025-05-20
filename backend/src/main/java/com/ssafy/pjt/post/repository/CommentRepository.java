package com.ssafy.pjt.post.repository;

import com.ssafy.pjt.post.dto.request.DeleteCommentRequestDto;
import com.ssafy.pjt.post.dto.request.EditCommentRequestDto;

public interface CommentRepository {
	Integer editComment(EditCommentRequestDto editCommentRequest);
	Integer deleteComment(DeleteCommentRequestDto deleteCommentRequest);
}
