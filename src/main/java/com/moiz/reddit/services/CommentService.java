package com.moiz.reddit.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moiz.reddit.Dto.CommentsDto;
import com.moiz.reddit.Exceptions.SpringRedditEception;
import com.moiz.reddit.Model.Comment;
import com.moiz.reddit.Model.NotificationEmail;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.User;
import com.moiz.reddit.mapper.CommentMapper;
import com.moiz.reddit.repository.CommentRepository;
import com.moiz.reddit.repository.PostRepository;
import com.moiz.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(() -> new SpringRedditEception("Id :"+commentsDto.getPostId()+" Post not found"));
		
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
		
		String message = mailContentBuilder.build(post.getUser().getUsername()+" posted a comment on your post");
		sendCommentNotification(message, post.getUser());
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername()+" Commented on your post", user.getEmail(), message));
	}

	public List<CommentsDto> getAllCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new SpringRedditEception("Post "+postId+" Not Found"));
		return commentRepository.findByPost(post)
				.stream()
				.map(commentMapper::mapTODto)
				.collect(Collectors.toList());
	}

	public List<CommentsDto> getAllCommentsForUser(String userName) {
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new SpringRedditEception(userName+" user Not Found"));
		
		return commentRepository.findAllByUser(user)
				.stream()
				.map(commentMapper::mapTODto)
				.collect(Collectors.toList());
	}
}
