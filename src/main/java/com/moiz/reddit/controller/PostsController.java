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

import com.moiz.reddit.Dto.PostsRequest;
import com.moiz.reddit.Dto.PostsResponse;
import com.moiz.reddit.services.PostsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostsController {

	private final PostsService postsService;
	
	@PostMapping
	public ResponseEntity<Void> createPost(@RequestBody PostsRequest postsRequest) {
		postsService.save(postsRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostsResponse> getPost(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(postsService.getPost(id));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<PostsResponse>> getAllPosts() {
		return ResponseEntity.status(HttpStatus.OK).body(postsService.getAllPosts());
	}
	
	@GetMapping("/by-subreddit/{id}")
	public ResponseEntity<List<PostsResponse>> getPostsBySubreddit(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(postsService.getPostsBySubreddit(id));
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<List<PostsResponse>> getPostsByUsername(@PathVariable String username) {
		return ResponseEntity.status(HttpStatus.OK).body(postsService.getPostsByUsername(username));
	}
	
	
}
