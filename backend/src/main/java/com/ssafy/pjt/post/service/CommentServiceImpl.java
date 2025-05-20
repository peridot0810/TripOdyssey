package com.ssafy.pjt.post.service;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.post.dto.request.DeleteCommentRequestDto;
import com.ssafy.pjt.post.dto.request.EditCommentRequestDto;
import com.ssafy.pjt.post.exception.DeleteCommentFailedException;
import com.ssafy.pjt.post.exception.EditCommentFailedException;
import com.ssafy.pjt.post.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	private final CommentRepository commentRepository;
	
	@Override
	public void editComment(String userId, String content, Integer commentId) {
		
		EditCommentRequestDto editCommentRequest = EditCommentRequestDto.builder()
				.writer(userId)
				.content(content)
				.commentId(commentId)
				.build();
		
		Integer edittedCommentCnt = commentRepository.editComment(editCommentRequest);
		
		// 수정된 댓글이 없다면 예외 던지기
		if(edittedCommentCnt == 0) {
			throw new EditCommentFailedException("댓글 작성자가 아니거나 존재하지 않는 댓글입니다.");
		}
	}
	
	@Override
	public void deleteComment(String userId, Integer commentId) {
		
		DeleteCommentRequestDto deleteCommentRequest = DeleteCommentRequestDto.builder()
				.userId(userId)
				.commentId(commentId)
				.build();
		Integer deletedCommentCnt = commentRepository.deleteComment(deleteCommentRequest);
		
		if(deletedCommentCnt == 0) {
			throw new DeleteCommentFailedException("댓글 작성자가 아니거나 존재하지 않는 댓글입니다.");
		}
	}
}
