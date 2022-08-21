package com.moiz.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.moiz.reddit.Dto.CommentsDto;
import com.moiz.reddit.Model.Comment;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text", source = "commentsDto.text")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	Comment map(CommentsDto commentsDto, Post post, User user);
	
	@Mapping(target = "postId", expression  = "java(comment.getPost().getPostId())")
	@Mapping(target = "userName", expression  = "java(comment.getUser().getUsername())")
	CommentsDto mapTODto(Comment comment);
}
