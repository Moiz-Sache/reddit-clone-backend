package com.moiz.reddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moiz.reddit.Dto.CommentsDto;
import com.moiz.reddit.services.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
	
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
		commentService.save(commentsDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/by-post/{postId}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));
	}
	
	@GetMapping("/by-user/{userName}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForUser(userName));
	}
}
