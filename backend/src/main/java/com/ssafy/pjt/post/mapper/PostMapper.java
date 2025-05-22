package com.ssafy.pjt.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.post.dto.request.CreateCommentRequestDto;
import com.ssafy.pjt.post.dto.request.CreatePostRequestDto;
import com.ssafy.pjt.post.dto.request.DeletePostRequestDto;
import com.ssafy.pjt.post.dto.request.EditPostRequestDto;
import com.ssafy.pjt.post.dto.request.GetPostRequestDto;
import com.ssafy.pjt.post.dto.request.LikePostRequestDto;
import com.ssafy.pjt.post.dto.request.PostFilterDto;
import com.ssafy.pjt.post.dto.response.GetPostResponseDto;
import com.ssafy.pjt.post.entity.Comment;

@Mapper
public interface PostMapper {
	// create
	void createPost(CreatePostRequestDto createPostRequest);
	void createComment(CreateCommentRequestDto createCommentRequest);
	
	// get
	List<GetPostResponseDto> getPostList(PostFilterDto searchCondition);
	GetPostResponseDto getPost(GetPostRequestDto getPostRequest);
	List<Comment> getComments(Integer postId);
	
	// put
	Integer editPost(EditPostRequestDto editPostRequest);
	
	// delete
	Integer deletePost(DeletePostRequestDto deletePostRequest);
	
	// like
	void likePost(LikePostRequestDto likePostRequest);
	void unlikePost(LikePostRequestDto likePostRequest);
}
