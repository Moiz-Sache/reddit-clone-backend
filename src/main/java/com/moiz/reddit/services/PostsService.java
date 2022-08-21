package com.moiz.reddit.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moiz.reddit.Dto.PostsRequest;
import com.moiz.reddit.Dto.PostsResponse;
import com.moiz.reddit.Exceptions.SpringRedditEception;
import com.moiz.reddit.Model.Post;
import com.moiz.reddit.Model.Subreddit;
import com.moiz.reddit.Model.User;
import com.moiz.reddit.mapper.PostMapper;
import com.moiz.reddit.repository.PostRepository;
import com.moiz.reddit.repository.SubredditRepository;
import com.moiz.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostsService {

	private final SubredditRepository subredditRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;
	
	public Post save(PostsRequest postsRequest) {
		Subreddit subreddit = subredditRepository.findByName(postsRequest.getSubredditName())
				.orElseThrow(() -> new SpringRedditEception(postsRequest.getSubredditName()+" not found"));
		User currentUser = authService.getCurrentUser();
		
		return postMapper.map(postsRequest, subreddit, currentUser);
		
	}

	@Transactional(readOnly = true)
	public PostsResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new SpringRedditEception("Post Not Found "+id.toString()));
		return postMapper.mapToDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostsResponse> getAllPosts() {
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

	public List<PostsResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SpringRedditEception("Subreddit not Find "+subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		return posts.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

	public List<PostsResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditEception(username+" Not Found"));
		List<Post> posts = postRepository.findByUser(user);
		return posts
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

}
